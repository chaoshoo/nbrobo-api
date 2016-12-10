package com.net.nky.seq;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysDb {

	private static Logger log = LoggerFactory.getLogger(SysDb.class);
	private static String SqlSeq1 = "SELECT ";
	private static String SqlSeq2 = ".nextval FROM dual";

	private static String SqlSequenceQuery = "SELECT " + "/* ";

	private static String SqlSequenceQuery2 = " */ class_name, seq_id, step_value, min_value, max_value "
			+ "FROM ioid_sequence_t WHERE 2=2 and class_name = ? " + "FOR UPDATE";

	private static String SqlSequenceUpdate = "UPDATE /* ";

	private static String SqlSequenceUpdate2 = " */ ioid_sequence_t SET seq_id = ? " + "WHERE  2=2 and  class_name = ?";

	static DataSource dataSource;

	public Long getSequenceOracle(String seqName, JdbcTemplate jdbcTemplate) {
		if (seqName == null || seqName.length() <= 0) {
			return null;
		}
		if (jdbcTemplate == null) {
			return null;
		}
		String sql = SqlSeq1 + seqName + SqlSeq2;
		Object o = jdbcTemplate.queryForObject(sql, Long.class);
		if (o != null && o instanceof Long) {
			return (Long) o;
		}
		return null;
	}

	/**取得List(SysSequence)列表，从ioid_sequence_t表取数据*/
	private SysSequence getSequence(Object[] params) {
		SequenceSqlQuery sql = new SequenceSqlQuery();
		List list = sql.execute(params);
		if (list != null && list.size() > 0) {
			return (SysSequence) list.get(0);
		}
		return null;
	}

	/**根据SQL更新一个数据列表，并返回更新的行数*/
	private int updateSequence(Object[] params) {
		SequenceSqlUpdate sql = new SequenceSqlUpdate();
		return sql.update(params);
	}

	//用于查询序列号表
	private class SequenceSqlQuery extends MappingSqlQuery {

		public SequenceSqlQuery() {
			super(dataSource, SysDb.SqlSequenceQuery + SysDb.SqlSequenceQuery2);
			declareParameter(new SqlParameter("class_name", Types.VARCHAR));
			compile();
		}

		protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysSequence po = new SysSequence();

			String className = rs.getString(1);
			Long seqId = new Long(rs.getLong(2));
			Integer stepValue = new Integer(rs.getInt(3));
			Long minValue = new Long(rs.getLong(4));
			Long maxValue = new Long(rs.getLong(5));

			po.setId(seqId);
			po.setClassName(className);
			po.setSeqId(seqId);
			po.setStepValue(stepValue);
			po.setMinValue(minValue);
			po.setMaxValue(maxValue);

			return po;
		}
	}

	//用于更新序列号表
	private class SequenceSqlUpdate extends SqlUpdate {

		public SequenceSqlUpdate() {
			super(dataSource, SysDb.SqlSequenceUpdate + SysDb.SqlSequenceUpdate2);
			declareParameter(new SqlParameter("seq_id", Types.NUMERIC));
			declareParameter(new SqlParameter("class_name", Types.VARCHAR));
			compile();
		}
	}

	/**在事务中存取序列号
	 * @throws Exception */
	public SysSequence getSequence(String seqName) throws Exception {
		if (seqName == null || seqName.length() <= 0) {
			throw new Exception("seqName不能为空");
		}
		//在事务中存取序列号
		SysSequence po = doInTransaction(seqName.toUpperCase(), new Object[] { seqName });

		return po;
	}

	@Transactional
	public SysSequence doInTransaction(String seqName, Object[] params) {

		//提取序列号
		SysSequence po = getSequence(params);
		if (po == null) {
			return null;
		}

		//取得序列号的步长
		Integer stepValue = po.getStepValue();
		if (stepValue == null) {
			return null;
		}

		//计算序列号的新值
		int step = Math.max(1, stepValue.intValue());
		long seqId = po.getSeqId().longValue();
		long startValue = seqId + 1;
		long nextValue = seqId + step;
		long upValue = nextValue + 1;

		//记录返回的序列号新值
		po.setStartValue(startValue);
		po.setEndValue(nextValue);

		//更新序列号的配置值
		Object[] values = new Object[] { new Long(upValue), seqName };
		updateSequence(values);

		return po;
	}

	//	public SysSequence getSequenceByStep(String seqName,int stepNum) throws Exception {
	//		if (seqName == null ) {
	//			throw new Exception("系统无法取得序列号。");
	//		}
	//		if (seqName.length() <= 0) {
	//			throw new Exception("系统无法取得序列号。");
	//		}
	//		
	//		//在事务中存取序列号
	//		SysSequence po = (SysSequence) transactionTemplate.execute
	//		(new SequenceTransactionCallbackByStep(seqName,stepNum));
	//		
	//		return po;
	//	}
	//	
	//	private class SequenceTransactionCallbackByStep{
	//
	//		private String seqName;
	//		private int stepNum;
	//		private Object[] params;
	//		
	//		public SequenceTransactionCallbackByStep(String seqName,int stepNum) {
	//			this.seqName = seqName.toUpperCase();
	//			this.stepNum=stepNum;
	//			this.params = new Object[] {this.seqName};
	//		}
	//		
	//		public Object doInTransaction(TransactionStatus status) {
	//			
	//			//提取序列号
	//			SysSequence po = getSequence(params);
	//			if (po == null) {
	//				return null;
	//			}
	//			
	//			long seqId = po.getSeqId().longValue();
	//			long startValue = seqId + 1;
	//			long nextValue = seqId + stepNum;
	//			long upValue = nextValue+1;
	//			
	//			//记录返回的序列号新值
	//			po.setStartValue(startValue);
	//			po.setEndValue(nextValue);
	//			
	//			//更新序列号的配置值
	//			Long rId = po.getRegionId();
	//			Object[] values = new Object[] {new Long(upValue), seqName, rId};
	//			updateSequence(values);
	//			return po;
	//		}
	//	}
}
