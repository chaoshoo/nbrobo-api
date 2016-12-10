package com.net.nky.seq;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * <h2>BasePo是所有PO类的抽象基类</h2>
 * </p>
 * 
 * <p>
 * 实现了对数据库表的主键字段、时间戳字段、状态字段的存取操作，实现了日志跟踪
 * 功能，实现了equals, hashCode, toString方法，实现了序列化功能
 * （Serializable）。
 * </p>
 * 
 * <p>
 * BasePo抽象基类要求每个数据库表有单一主键字段：ID Number(20)，时间戳
 * 字段：STAMP Date，状态字段：STATUS Number(20)，数据库表的主键字段
 * 应该是没有任何实际意义的流水号，时间戳字段应该在每次保存数据时更新，且不
 * 应该有其他含义，状态字段应该至少有两个状态：0-删除，1-正常。
 * </p>
 * 
 * <p>
 * 实现PO类时，必须从BasePo基类继承，命名为Xxx（不必以Po结尾），并且配置在
 * Hibernate影射文件中，文件名为Xxx.hbm.xml，每个PO影射文件中都需要配置
 * 主键、时间戳、状态属性。PO类和相关影射文件放在相同的目录。可以使用简单的一
 * 对多、多对一关联，如果使用了一对多关联，请使用Bag标签进行配置。
 * </p>
 * 
 * <p>
 * 实现PO类时，对于新增的基本属性可使用以下数据类型：字符串使用java.lang.String类，
 * 日期使用java.util.Date，整数使用java.lang.Integer或者java.lang.Long，
 * 浮点数使用java.math.BigDecimal。
 * </p>
 * 
 * <p>
 * 实现PO类时，不必实现Serializable接口，因为BasePo基类已经实现。也不必
 * 重新定义id属性和stamp属性，不必重写equals, hashCode, toString方法，
 * 不必定义日志对象，如果需要写日志，则直接调用BasePo基类的getLog()方法获
 * 得Log对象即可。
 * </p>
 * 
 * <p>
 * PO对象的生命周期应该由Dao层统一管理，包括新增、保存、查询等操作，不应该在
 * Dao层的上层创建PO对象，不能使用new Xxx()的方式创建PO，如果要创建PO，则
 * 必须调用对应Dao的create()方法，以获得关键的初始值，如主键。从数据库中删
 * 除PO时，不应该直接删除，而应该使用设置状态的方式实现。
 * </p>
 * 
 * <p>
 * 在Dao层查询出PO对象时，PO对象处于持久化状态（与Session关联），必须调用
 * evict()方法使PO处于脱管状态（与Session脱离），然后才可以传递到Service
 * 层或者其他上层。Dao层保存PO对象后，也需要调用evict()方法。
 * </p>
 * 
 * <p>
 * PO对象不可以包含DTO对象，如果PO需要组装成DTO，则应该在Service层组装，而
 * 不应该在Dao或者Action层编写组装逻辑代码。
 * </p>
 * 
 * <p>
 * PO类可以传递到ActionForm类，并作为ActionForm类的属性使用，以减少
 * 不必要的get/set方法调用。
 * </p>
 * 
 */
public abstract class BasePo extends BaseDto {

	//主键类的元数据
	private Class pkClazz = null;
	
	//表示PO实例对象是否是新建的
	private boolean newer = false;
	
	//主键属性：ID
	private Long id;

	//主键属性：PK
	private BasePk pk;

	//状态属性：STATUS
	private String state;

	//状态时间属性
	private Date stateDate;
	
	//时间戳
	private Timestamp stamp;
	
	public BasePo() {
	}
	
	public BasePo(Class pkClazz) {
		setPkClazz(pkClazz);
	}
	
	/**建立主键属性：PK*/
	public BasePk newPk() {
		if (getPkClazz() == null) {
			return null;
		}
		
		BasePk p = null;
		try {
			p = (BasePk) getPkClazz().newInstance();
		} catch (Throwable t) {
			Exception eb = new Exception("创建联合主键实例失败", t);
			getLog().error(eb.getMessage());
		}
		return p;
	}
	
	/**取得主键属性：PK*/
	public BasePk getPk() {
		return pk;
	}

	/**设置主键属性：PK*/
	public void setPk(BasePk pk) {
		this.pk = pk;
	}

	/**取得主键属性：ID*/
	public Long getId() {
		return id;
	}

	/**设置主键属性：ID*/
	public void setId(Long id) {
		this.id = id;
	}

	/**取得状态属性：STATUS*/
	public String getState() {
		return state;
	}

	/**设置状态属性：STATUS*/
	public void setState(String state) {
		this.state = state;
	}

	/**取得状态时间属性：STATUS_DATE*/
	public Date getStateDate() {
		return stateDate;
	}

	/**设置状态时间属性：STATUS_DATE*/
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	/**表示PO实例对象是否是新建的*/
    public boolean isNewer() {
		return newer;
	}

	/**设置PO实例对象是否是新建的*/
	public void setNewer(boolean newer) {
		this.newer = newer;
	}

	/**数据库数据的实体状态：作废，值："10X"*/
	public String getStateDiscard() {
		return "10X";
	}

	/**数据库数据的实体状态：正常，值："10A"*/
	public String getStateNormal() {
		return "10A";
	}

	/**取得主键类的元数据*/
	protected Class getPkClazz() {
		return pkClazz;
	}

	/**设置主键类的元数据*/
	protected void setPkClazz(Class pkClazz) {
		this.pkClazz = pkClazz;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}

}
