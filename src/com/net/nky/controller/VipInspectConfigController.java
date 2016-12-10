package com.net.nky.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.net.nky.entity.InspectDic;
import com.net.nky.entity.InspectKpiConfig;
import com.net.nky.entity.Office;
import com.net.nky.singleton.InspectConfigData;
import com.net.nky.singleton.OfficeSingleton;

/**
 * 使用示例：
1.获取所有的检测指标 C01: C02 . 包括他们下面的指标及阀值
http://localhost:8080/nkyplatform/vipInspectConfig/getAllInspect.json
2.获取所有某一个检测指标代码的数据 C01下面的指标及阀值
http://localhost:8080/nkyplatform/vipInspectConfig/getInspect/C01.json
3.获取所有某一个检测指标代码的数据 C01下面的指标SYS de 阀值
http://localhost:8080/nkyplatform/vipInspectConfig/getInspectValues/C01/SYS.json
 * 检测指标接口.
 * @author Ken
 * @version 2016年8月22日
 */
@Controller
@RequestMapping(value = "/vipInspectConfig")
public class VipInspectConfigController {

	/**
	 * http://localhost:8080/nkyplatform/vipInspectConfig/officeall.json
	 * {"1001":{"code":"1001","name":"骨科","pic":"/nkywx/img/icon_niaosuan.png","des":null,"description":null},"1003":{"code":"1003","name":"泌尿科","pic":null,"des":null,"description":null},"1002":{"code":"1002","name":"胸图","pic":null,"des":null,"description":null},"1004":{"code":"1004","name":"心脏科","pic":null,"des":null,"description":null}}
	 */
	@RequestMapping(value = "/officeall", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Office> officeall() {
		return OfficeSingleton.getInstance().getAll();
	}
	
	/**
	 * http://localhost:8080/nkyplatform/vipInspectConfig/office/1001.json
	 * {"code":"1001","name":"骨科","pic":"/nkywx/img/icon_niaosuan.png","des":null,"description":null}
	 */
	@RequestMapping(value = "/office/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Office office(@PathVariable("code") String code) {
		return OfficeSingleton.getInstance().getEntitybykey(code);
	}
	
	/**
	 * 获取所有的检测指标 C01: C02 . 包括他们下面的指标
	 * {
	"C01": {
	    "dicName": "C01",
	    "dicValue": "血压",
	    "inspectMap": {
	        "PR": {
	            "code": "PR",
	            "name": "脉率",
	            "isfz": null,
	            "unit": "bpm",
	            "inspectCode": "C01",
	            "des": null,
	            "kpiMax": null,
	            "kpiMin": null,
	            "kpiPic": null,
	            "fzSet": []
	        },
	        "SYS": {
	            "code": "SYS",
	            "name": "收缩压",
	            "isfz": null,
	            "unit": "mmHg",
	            "inspectCode": "C01",
	            "des": null,
	            "kpiMax": "90",
	            "kpiMin": "180",
	            "kpiPic": null,
	            "fzSet": [
	                {
	                    "kipCode": "SYS",
	                    "sex": "0",
	                    "ageMin": 30,
	                    "ageMax": 50,
	                    "fzMax": 130,
	                    "fzMin": 150
	                },
	                {
	                    "kipCode": "SYS",
	                    "sex": "0",
	                    "ageMin": 10,
	                    "ageMax": 20,
	                    "fzMax": 120,
	                    "fzMin": 140
	                },
	                {
	                    "kipCode": "SYS",
	                    "sex": "0",
	                    "ageMin": 20,
	                    "ageMax": 30,
	                    "fzMax": 110,
	                    "fzMin": 130
	                }
	            ]
	        },
	        "DIA": {
	            "code": "DIA",
	            "name": "舒张压",
	            "isfz": null,
	            "unit": "mmHg",
	            "inspectCode": "C01",
	            "des": null,
	            "kpiMax": null,
	            "kpiMin": null,
	            "kpiPic": null,
	            "fzSet": []
	        }
	    }
	},
	"C02": {
	    "dicName": "C02",
	    "dicValue": "血糖",
	    "inspectMap": {
	        "GLU2": {
	            "code": "GLU2",
	            "name": "餐后血糖",
	            "isfz": null,
	            "unit": "mmol/L",
	            "inspectCode": "C02",
	            "des": null,
	            "kpiMax": null,
	            "kpiMin": null,
	            "kpiPic": null,
	            "fzSet": []
	        },
	        "GLU1": {
	            "code": "GLU1",
	            "name": "餐前血糖",
	            "isfz": null,
	            "unit": "mmol/L",
	            "inspectCode": "C02",
	            "des": null,
	            "kpiMax": null,
	            "kpiMin": null,
	            "kpiPic": null,
	            "fzSet": []
	        },
	        "GLU0": {
	            "code": "GLU0",
	            "name": "随机血糖",
	            "isfz": null,
	            "unit": "mmol/L",
	            "inspectCode": "C02",
	            "des": null,
	            "kpiMax": null,
	            "kpiMin": null,
	            "kpiPic": null,
	            "fzSet": []
	        }
	    }
	}
	}
	 */
	@RequestMapping(value = "/getAllInspect", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, InspectDic> getAllInspect() {
		return InspectConfigData.getInstance().getAllInspect();
	}

	/**
	 * 获取所有某一个检测指标代码的数据 C01下面的指标
	 * {
	"dicName": "C01",
	"dicValue": "血压",
	"inspectMap": {
	    "PR": {
	        "code": "PR",
	        "name": "脉率",
	        "isfz": null,
	        "unit": "bpm",
	        "inspectCode": "C01",
	        "des": null,
	        "kpiMax": null,
	        "kpiMin": null,
	        "kpiPic": null,
	        "fzSet": []
	    },
	    "SYS": {
	        "code": "SYS",
	        "name": "收缩压",
	        "isfz": null,
	        "unit": "mmHg",
	        "inspectCode": "C01",
	        "des": null,
	        "kpiMax": "90",
	        "kpiMin": "180",
	        "kpiPic": null,
	        "fzSet": [
	            {
	                "kipCode": "SYS",
	                "sex": "0",
	                "ageMin": 30,
	                "ageMax": 50,
	                "fzMax": 130,
	                "fzMin": 150
	            },
	            {
	                "kipCode": "SYS",
	                "sex": "0",
	                "ageMin": 10,
	                "ageMax": 20,
	                "fzMax": 120,
	                "fzMin": 140
	            },
	            {
	                "kipCode": "SYS",
	                "sex": "0",
	                "ageMin": 20,
	                "ageMax": 30,
	                "fzMax": 110,
	                "fzMin": 130
	            }
	        ]
	    },
	    "DIA": {
	        "code": "DIA",
	        "name": "舒张压",
	        "isfz": null,
	        "unit": "mmHg",
	        "inspectCode": "C01",
	        "des": null,
	        "kpiMax": null,
	        "kpiMin": null,
	        "kpiPic": null,
	        "fzSet": []
	    }
	}
	}
	 */
	@RequestMapping(value = "/getInspect/{code}", method = RequestMethod.GET)
	@ResponseBody
	public InspectDic getInspect(@PathVariable("code") String code) {
		return InspectConfigData.getInstance().getInspect(code);
	}

	/**
	 * 获取所有某一个检测指标代码的数据 C01下面的指标
	 * {
	"code": "SYS",
	"name": "收缩压",
	"isfz": null,
	"unit": "mmHg",
	"inspectCode": "C01",
	"des": null,
	"kpiMax": "90",
	"kpiMin": "180",
	"kpiPic": null,
	"fzSet": [
	    {
	        "kipCode": "SYS",
	        "sex": "0",
	        "ageMin": 20,
	        "ageMax": 30,
	        "fzMax": 110,
	        "fzMin": 130
	    },
	    {
	        "kipCode": "SYS",
	        "sex": "0",
	        "ageMin": 10,
	        "ageMax": 20,
	        "fzMax": 120,
	        "fzMin": 140
	    },
	    {
	        "kipCode": "SYS",
	        "sex": "0",
	        "ageMin": 30,
	        "ageMax": 50,
	        "fzMax": 130,
	        "fzMin": 150
	    }
	]
	}
	 */
	@RequestMapping(value = "/getInspectValues/{inspectCode}/{kpiCode}", method = RequestMethod.GET)
	@ResponseBody
	public InspectKpiConfig getInspectValues(@PathVariable("inspectCode") String inspectCode,
			@PathVariable("kpiCode") String kpiCode) {
		return InspectConfigData.getInstance().getInspectValues(inspectCode, kpiCode);
	}

}
