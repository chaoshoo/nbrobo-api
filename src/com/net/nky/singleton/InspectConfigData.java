package com.net.nky.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.net.nky.entity.InspectDic;
import com.net.nky.entity.InspectKpiConfig;
import com.net.nky.entity.InspectKpiConfigFz;

/**
 * 检查指标接口.
 * @author Ken
 * @version 2016年8月22日
 */
public class InspectConfigData {

	private static final Logger LOG = LoggerFactory.getLogger(InspectConfigData.class);

	private static InspectConfigData instance = null;

	//C01 	血压		 
	private static ConcurrentHashMap<String, InspectDic> INSPECT_CODE_NAME_MAP = new ConcurrentHashMap<String, InspectDic>();

	private static Boolean LOCK = new Boolean(true);

	//我通过C01 可以知道他下面  有那些指标  
	//然后传入指标 可以知道指标熟悉及阈值范围
	
	/**
	 * 获取单例.
	 */
	public static InspectConfigData getInstance() {
		if (instance == null) {
			instance = new InspectConfigData();
		}
		return instance;
	}

	/**
	 * 获取所有的检测指标 C01: C02 . 包括他们下面的指标
	 */
	public Map<String, InspectDic> getAllInspect(){
		return INSPECT_CODE_NAME_MAP;
	}

	/**
	 * 获取所有某一个检测指标代码的数据 C01下面的指标
	 */
	public InspectDic getInspect(String code){
		return INSPECT_CODE_NAME_MAP.get(code);
	}

	/**
	 * 获取所有某一个检测指标 C01 下面的指标阀值列表
	 */
	public InspectKpiConfig getInspectValues(String inspectCode,String kpiCode){
		return INSPECT_CODE_NAME_MAP.get(inspectCode).getInspectMap().get(kpiCode);
	}
	
	private InspectConfigData() {
		loadData();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		synchronized (LOCK) {
			List<Record> list = Db.find("select DIC_NAME,DIC_VALUE from dic where DIC_TYPE = 'inspect_code'");
			for (Record r : list) {
				InspectDic dic = new InspectDic();
				dic.setDicName(r.getStr("DIC_NAME"));
				dic.setDicValue(r.getStr("DIC_VALUE"));
				INSPECT_CODE_NAME_MAP.put(dic.getDicName(), dic);
			}
			Map<String, String> KPI_INSPEC_CODE = new HashMap<String, String>();//记录配置中的代码和指标的反向关系
			List<Record> configList = Db
					.find("SELECT CODE,NAME,UNIT,INSPECT_CODE,KPI_MAX,KPI_MIN,KPI_PIC FROM inspect_kpi_config ");
			for (Record r : configList) {
				String inspectCode = r.getStr("INSPECT_CODE");
				InspectDic dic = INSPECT_CODE_NAME_MAP.get(inspectCode);
				if (dic == null) {
					LOG.warn(inspectCode + "未有配置到字典表DIC，忽略掉...");
					continue;
				}
				InspectKpiConfig kpi = new InspectKpiConfig();
				kpi.setCode(r.getStr("CODE"));
				kpi.setName(r.getStr("NAME"));
				kpi.setUnit(r.getStr("UNIT"));
				kpi.setKpiMax(r.getStr("KPI_MAX"));
				kpi.setKpiMin(r.getStr("KPI_MIN"));
				kpi.setKpiPic(r.getStr("KPI_PIC"));
				kpi.setInspectCode(r.getStr("INSPECT_CODE"));
				KPI_INSPEC_CODE.put(kpi.getCode(), inspectCode);// 
				dic.getInspectMap().put(kpi.getCode(), kpi);
				LOG.debug("添加配置:" + kpi);
			}
			//select KIP_CODE,SEX,AGE_MIN,AGE_MAX,FZ_MAX,FZ_MIN from inspect_kpi_config_fz 
			List<Record> configChildList = Db.find("select * from inspect_kpi_config_fz ");
			for (Record r : configChildList) {
				String configCode = r.getStr("KIP_CODE");
				String inspectCode = KPI_INSPEC_CODE.get(configCode);
				InspectDic dic = INSPECT_CODE_NAME_MAP.get(inspectCode);
				if (dic == null) {
					LOG.warn(inspectCode + "未有配置到字典表DIC，忽略掉..." + configCode);
					continue;
				}

				InspectKpiConfigFz fz = new InspectKpiConfigFz();
				fz.setAgeMax(r.getInt("AGE_MAX"));
				fz.setAgeMin(r.getInt("AGE_MIN"));
				fz.setSex(r.getStr("SEX"));
				fz.setFzMax(r.getStr("FZ_MAX"));
				fz.setFzMin(r.getStr("FZ_MIN"));
				fz.setKipCode(r.getStr("KIP_CODE"));
				InspectKpiConfig config = dic.getInspectMap().get(configCode); 
				config.getFzSet().add(fz);
				LOG.debug("添加配置辅助:" + fz);
			}
		}
	}

	/**
	 * 重新加载
	 */
	public void reload() {
		loadData();
	}

}
