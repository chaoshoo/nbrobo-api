package com.net.nky.seq;

import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.net.util.SpringUtil;

public final class SysId {
	
	private static SysDb sysDb;
	
	private static SysId sysId = null;

	private static Log log = LogFactory.getLog(SysId.class);
	
	//支持多个序列号，将序列号名称和取序列号的SQL放在该影射中
	private static Map seqMap = new HashMap();
	
	//不允许构造SysId类
	private SysId() {}
	
	public static SysId getInstance() {
		if (sysId == null) {
			sysId = new SysId();
		}
		if (sysDb == null) {
			sysDb = (SysDb) SpringUtil.getBean("sysDb");
			DataSource dataSource=(DataSource)SpringUtil.getBean("dataSource");
			SysDb.dataSource=dataSource;
		}
		return sysId;
	}
	
	/**通过主键序列号定义表取得主键:SEQ_ID
	 * @throws Exception */
	public Long getId() throws Exception {
		return getSeq("SEQ_ID");
	}

	/**通过主键序列号定义表取得主键
	 * @throws Exception */
	public Long getId(String seqName) throws Exception {
		return getSeq(seqName);
	}
//	/**通过主键序列号定义表取得主键
//	 * @throws Exception */
//	public static SysSequence getIdByStep(String seqName,int step) throws Exception {
//		SysSequence po = sysDb.getSequenceByStep(seqName,step);
//		return po;
//	}
	

	private static Long getSeq(String seqName) throws Exception {
		
		String seq = seqName;
		if (seqName == null || seqName.length() <= 0) {
			seq = "SEQ_ID";
		}
		
		//取得缓存的序列号
		PrimaryKey pk = (PrimaryKey) seqMap.get(seq);
		if (pk == null) {
			pk = new PrimaryKey();
			seqMap.put(seq, pk);
		}
		
		//取得缓存的序列号值
		Long id = nextSeq(seq, pk);
		
		if (log.isDebugEnabled()) {
			log.debug("取得序列号(" + seqName + ")的值：" + id);
		}
		
		return id;
	}
	
	//取得缓存的序列号值
	private static Long nextSeq(String seqName, PrimaryKey pk) throws Exception {
		synchronized (pk) {
			Long id = null;
			id = pk.next();
			long seqStart = 0;
			long seqEnd = 0;
			if (id == null) {
				//没有取得序列号
				try {
					SysSequence po = sysDb.getSequence(seqName);
					long start = po.getStartValue();
					long end = po.getEndValue();
					pk.setId(start);
					pk.setMaxId(end);
					id = pk.next();
					seqStart = start;
					seqEnd = end;
//					log.debug("GET SEQ OUTPUT PARAMS !start = " + start + ";end= " + end + ";id= " + id);
				} catch (Throwable t) {
					throw new RuntimeException("取得序列号失败: " + seqName, t);
				}
			}
			log.debug("GET SEQ OUTPUT PARAMS ! seqName = " + seqName + ",id= " + id +"start = " + seqStart + ",end= " + seqEnd );
			return id;
		}
	}
	
	
	public void setSysDb(SysDb sysDb) {
		SysId.sysDb = sysDb;
	}

	//缓存序列号的内部类
	private static class PrimaryKey {
		
		public PrimaryKey() {}
		
		private long id = 0L;
		private long maxId = -1L;
		
		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}
		
		public void setId(long id) {
			this.id = id;
		}
		
		public Long next() {
			if (id <= maxId) {
				return new Long(id++);
			}
			return null;
		}
	}
	
	/**
	 * 取医院的code
	 * @return
	 * @throws Exception
	 */
	public static String  getNextHospitalCode() throws Exception{
			return "H"+getInstance().getSeq("SEQ_HOSPITAL_ID");
		
	}
	
	public static String  getNextDoctorCode() throws Exception{
		return "D"+getInstance().getSeq("SEQ_DOCTOR_ID");
	}
	
	public static String  getNextVipCode() throws Exception{
		return "V"+getInstance().getSeq("SEQ_VIP_ID");
	}	
}
