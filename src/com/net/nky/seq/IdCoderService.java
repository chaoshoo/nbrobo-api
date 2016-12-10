package com.net.nky.seq;

import org.springframework.stereotype.Service;

/**
 * 统一编码ID
 * @author lenovo
 *
 */
@Service(value="idCoderService")
public class IdCoderService {
//	@Autowired
//	private SysUserDao sysUserDao;
	

//	统一编码  
//	系统用户  SXXXX  例如 S0001
//	医院用户  Hxxxx
//	医生用户   Dxxxx
//	客户     Vxxxx
//	
	/**
	 * 获取用户统一编码
	 * @return
	 * @throws Exception 
	 */
	public String getVip() throws Exception {
		String id = "V";
		id+=SysId.getInstance().getId("SEQ_SYS_ID");
		return id;
	}
	
	
	/**
	 * 获取系统用户编码
	 * @return
	 * @throws Exception 
	 */
	public String getSys() throws Exception {
		String id = "S";
		id+=SysId.getInstance().getId("SEQ_SYS_ID");
		return id;
	}

	
	/**
	 * 获取医院编码
	 * @return
	 * @throws Exception 
	 */
	public String getHospital() throws Exception {
		String id = "H";
		id+=SysId.getInstance().getId("SEQ_HOSPITAL_ID");
		return id;
	}
	
	/**
	 * 获取医生编码
	 * @return
	 * @throws Exception 
	 */
	public String getPartnerChild() throws Exception {
		String id = "D";
		id+=SysId.getInstance().getId("SEQ_DOCTOR_ID");
		return id;
	}
	/**
	 * 获取供销商编码
	 * @return
	 * @throws Exception 
	 */
	public String getSupplyClientCode() throws Exception {
		String id = "V";
		id+=SysId.getInstance().getId("SEQ_VIP_ID");
		return id;
	}
	
}
