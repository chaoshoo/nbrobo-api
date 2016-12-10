package com.net.nky.garea.util;

/**
 * 一般就是按I II III aVR aVL aVF V1 V2 V3 V4 V5 V6顺序显示吧
 * 传给你们的是12导的数据  你们需要把这12导数据还原成波形显示，一般就是刚才那顺序纵向排列
 * 
 * 12导联的 逻辑 还是这样的  
 * 但这种方式显示  确实不敢恭维 
 * 我以前对接过别人的心电图  都是拿到12导联的 数据   通过代码 自己绘制图形  
 * @author Ken
 * @version 2016年9月23日
 */
public class EcgUtil {
	
    public static String getResultText(int code) {

		switch ((int) code) {
		case -1:
			return null;
		case 0:
			return "心律失常,传导阻滞,心肌梗死,ST & T异常,心室肥厚,心房肥厚,心电轴异常";
		case 1100:
			return "窦性心律";
		case 1120:
			return "窦性心动过速";
		case 1130:
			return "窦性心动过缓";
		case 1102:
			return "窦性心律不齐";
		case 1108:
			return "极端的窦性心律不齐";
		case 1200:
			return "房性心律";
		case 1220:
			return "房性心动过速";
		case 1210:
			return "心房颤动";
		case 12101:
			return "过速性心房颤动";
		case 12102:
			return "过缓性心房颤动";
		case 12103:
			return "心房颤动合并室性期前收缩或室内差异传导";
		case 12108:
			return "过速性心房颤动合并室性期前收缩或室内差异传导";
		case 12109:
			return "过缓性心房颤动合并室性期前收缩或室内差异传导";
		case 1250:
			return "心房扑动";
		case 12503:
			return "心房扑动合并室性期前收缩或室内差异传导";
		case 12505:
			return "心房扑动不排";
		case 1300:
			return "交界性心律";
		case 1320:
			return "交界性心动过速";
		case 1330:
			return "交界性心动过缓";
		case 1400:
			return "室上性心律";
		case 1420:
			return "室上性心动过速";
		case 1430:
			return "室上性心动过缓";
		case 1470:
			return "偶发性室上性期前收缩";
		case 1474:
			return "频发性室上性期前收缩";
		case 1475:
			return "频发性室上性期前收缩形成二联律";
		case 1500:
			return "室性心律";
		case 1520:
			return "室性心动过速";
		case 1570:
			return "偶发性室性期前收缩";
		case 1574:
			return "频发性室性期前收缩";
		case 1575:
			return "频发性室性期前收缩形成二联律";
		case 1970:
			return "偶发性异位性期前收缩";
		case 1974:
			return "频发性异位性期前收缩";
		case 1975:
			return "频发性异位性期前收缩形成二联律";
		case 16006:
			return "AAI起搏心电图";
		case 16007:
			return "VVI起搏心电图";
		case 1901:
			return "无法判断的规则心律";
		case 1902:
			return "无法判断的心律";
		case 2210:
			return "PR间期缩短";
		case 2216:
			return "A型WPW综合症";
		case 2217:
			return "B型WPW综合症";
		case 2218:
			return "WPW综合症";
		case 2219:
			return "间歇性WPW综合症";
		case 2231:
			return "I度房室阻滞";
		case 2232:
			return "II度房室阻滞，莫氏I性";
		case 2233:
			return "II度房室阻滞，莫氏II性";
		case 2420:
			return "右室传导延迟";
		case 2440:
			return "不完全右束支传导阻滞";
		case 2450:
			return "完全右束支传导阻滞";
		case 24501:
			return "完全右束支传导阻滞合并右室肥厚";
		case 2540:
			return "不完全左束支传导阻滞";
		case 2550:
			return "完全左束支传导阻滞";
		case 2630:
			return "左前分支传导阻滞";
		case 2730:
			return "左后分支传导阻滞";
		case 2320:
			return "心室内传导延迟";
		case 2330:
			return "心室内传导阻滞";
		case 9110:
			return "** 正常心电图 **";
		case 9120:
			return "** 非典型心电图 **";
		case 9130:
			return "** 可疑心电图 **";
		case 9140:
			return "** 异常心电图 **";
		case 5120:
			return "可疑右心室肥厚";
		case 5130:
			return "右心室肥厚";
		case 5134:
			return "右心室肥厚，伴ST-T改变";
		case 5211:
			return "左心室肥厚待排";
		case 5220:
			return "左心室肥厚待排";
		case 5222:
			return "可疑左心室肥厚";
		case 5233:
			return "左心室肥厚";
		case 5234:
			return "左心室肥厚，伴ST-T改变";
		case 3113:
			return "陈旧性前壁心肌梗死待排";
		case 3112:
			return "亚急性前壁心肌梗死待排";
		case 3111:
			return "急性前壁心肌梗死待排";
		case 3123:
			return "可疑陈旧性前壁心肌梗死";
		case 3122:
			return "可疑亚急性前壁心肌梗死";
		case 3121:
			return "可疑急性前壁心肌梗死";
		case 3133:
			return "陈旧性前壁心肌梗死";
		case 3132:
			return "亚急性前壁心肌梗死";
		case 3131:
			return "急性前壁心肌梗死";
		case 3413:
			return "陈旧性前间壁心肌梗死待排";
		case 3412:
			return "亚急性前间壁心肌梗死待排";
		case 3411:
			return "急性前间壁心肌梗死待排";
		case 3423:
			return "可疑陈旧性前间壁心肌梗死";
		case 3422:
			return "可疑亚急性前间壁心肌梗死";
		case 3421:
			return "可疑急性前间壁心肌梗死";
		case 3433:
			return "陈旧性前间壁心肌梗死";
		case 3432:
			return "亚急性前间壁心肌梗死";
		case 3431:
			return "急性前间壁心肌梗死";
		case 3513:
			return "陈旧性侧壁心肌梗死待排";
		case 3512:
			return "亚急性侧壁心肌梗死待排";
		case 3511:
			return "急性侧壁心肌梗死待排";
		case 3523:
			return "可疑陈旧性侧壁心肌梗死";
		case 3522:
			return "可疑亚急性侧壁心肌梗死";
		case 3521:
			return "可疑急性侧壁心肌梗死";
		case 3533:
			return "陈旧性侧壁心肌梗死";
		case 3532:
			return "亚急性侧壁心肌梗死";
		case 3531:
			return "急性侧壁心肌梗死";
		case 3613:
			return "陈旧性下壁心肌梗死待排";
		case 3612:
			return "亚急性下壁心肌梗死待排";
		case 3611:
			return "急性下壁心肌梗死待排";
		case 3623:
			return "可疑陈旧性下壁心肌梗死";
		case 3622:
			return "可疑亚急性下壁心肌梗死";
		case 3621:
			return "可疑急性下壁心肌梗死";
		case 3633:
			return "陈旧性下壁心肌梗死";
		case 3632:
			return "亚急性下壁心肌梗死";
		case 3631:
			return "急性下壁心肌梗死";
		case 36132:
			return "陈旧性下壁及后壁心肌梗死待排";
		case 36122:
			return "亚急性下壁及后壁心肌梗死待排";
		case 36112:
			return "急性下壁及后壁心肌梗死待排";
		case 36232:
			return "可疑陈旧性下壁及后壁心肌梗死";
		case 36222:
			return "可疑亚急性下壁及后壁心肌梗死";
		case 36212:
			return "可疑急性下壁及后壁心肌梗死";
		case 36332:
			return "陈旧性下壁及后壁心肌梗死";
		case 36322:
			return "亚急性下壁及后壁心肌梗死";
		case 36312:
			return "急性下壁及后壁心肌梗死";
		case 31131:
			return "V3/V4异常Q波（心肌梗死的可能性待排）";
		case 34131:
			return "V1/V2异常Q波（心肌梗死的可能性待排）";
		case 35131:
			return "I/AVL/V5/V6异常Q波（心肌梗死的可能性待排）";
		case 36131:
			return "II/AVF异常Q波（心肌梗死的可能性待排）";
		case 4038:
			return "ST段抬高";
		case 40304:
			return "急性心包炎";
		case 4164:
			return "可疑前壁心肌缺血";
		case 4165:
			return "前壁心肌缺血";
		case 4564:
			return "可疑侧壁心肌缺血";
		case 4565:
			return "侧壁心肌缺血";
		case 4664:
			return "可疑下壁心肌缺血";
		case 4665:
			return "下壁心肌缺血";
		case 4136:
			return "可疑前壁心肌损伤";
		case 4137:
			return "前壁心肌损伤";
		case 4536:
			return "可疑侧壁心肌损伤";
		case 4537:
			return "侧壁心肌损伤";
		case 4636:
			return "可疑下壁心肌损伤";
		case 4637:
			return "下壁心肌损伤";
		case 40106:
			return "ST段降低，可能为洋地黄作用";
		case 4011:
			return "轻度的ST段低下";
		case 40116:
			return "轻度的ST段低下，可能为洋地黄作用";
		case 4012:
			return "中度的ST段低下";
		case 40126:
			return "中度的ST段低下，可能为洋地黄作用";
		case 4016:
			return "显著的ST段低下，可能为心肌损伤";
		case 40166:
			return "显著的ST段低下，可能为心肌损伤或洋地黄作用";
		case 4017:
			return "显著的ST段低下，符合心肌损伤";
		case 4021:
			return "ST段J点型降低，可能正常";
		case 4023:
			return "ST段J点型降低";
		case 40302:
			return "ST段抬高，可能为早期复极综合症";
		case 40303:
			return "早期复极综合症";
		case 4050:
			return "T波高耸，可能为高血钾症";
		case 4068:
			return "T波异常";
		case 40686:
			return "非特异性的T波异常，可能为洋地黄作用";
		case 4436:
			return "可疑前间壁心肌损伤";
		case 4437:
			return "前间壁心肌损伤";
		case 6120:
			return "可疑右心房肥厚";
		case 6130:
			return "右心房肥厚";
		case 6220:
			return "可疑左心房肥厚";
		case 6230:
			return "左心房肥厚";
		case 7102:
			return "电轴轻度右偏";
		case 7100:
			return "电轴右偏";
		case 7202:
			return "电轴轻度左偏";
		case 7200:
			return "电轴左偏";
		case 7300:
			return "电轴重度右偏";
		case 7400:
			return "S1-S2-S3综合症";
		case 7500:
			return "QRS-T夹角异常";
		case 8003:
			return "可疑慢性肺原性心脏病";
		case 8100:
			return "低电压";
		case 8101:
			return "肢导联低电压";
		case 8102:
			return "胸导联低电压";
		case 8200:
			return "右位心";
		case 8304:
			return "QTc间期延长";
		case 8305:
			return "QTc间期短缩";
		case 101:
			return "左右上肢电极反接";
		case 4048:
			return "ST-T异常";
		case 16008:
			return "DDD起搏心电图";
		case 16009:
			return "起搏心电图";
		}
		return null;
	}
    
    public static String getCode(String result) {

    	if(result.equals("心律失常,传导阻滞,心肌梗死,ST & T异常,心室肥厚,心房肥厚,心电轴异常")){
    		return "0";
    	}else if(result == null || result == ""){
    		return "-1";
    	}else if(result.equals("窦性心律")){
    		return "1100";
    	}else if(result.equals("窦性心动过速")){
    		return "1120";
    	}else if(result.equals("窦性心动过缓")){
    		return "1130";
    	}else if(result.equals("窦性心律不齐")){
    		return "1102";
    	}else if(result.equals("极端的窦性心律不齐")){
    		return "1108";
    	}else if(result.equals("房性心律")){
    		return "1200";
    	}else if(result.equals("房性心动过速")){
    		return "1220";
    	}else if(result.equals("心房颤动")){
    		return "1210";
    	}else if(result.equals("过速性心房颤动")){
    		return "12101";
    	}else if(result.equals("过缓性心房颤动")){
    		return "12102";
    	}else if(result.equals("心房颤动合并室性期前收缩或室内差异传导")){
    		return "12103";
    	}else if(result.equals("过速性心房颤动合并室性期前收缩或室内差异传导")){
    		return "12108";
    	}else if(result.equals("过缓性心房颤动合并室性期前收缩或室内差异传导")){
    		return "12109";
    	}else if(result.equals("心房扑动")){
    		return "1250";
    	}else if(result.equals("心房扑动合并室性期前收缩或室内差异传导")){
    		return "12503";
    	}else if(result.equals("心房扑动不排")){
    		return "12505";
    	}else if(result.equals("交界性心律")){
    		return "1300";
    	}else if(result.equals("交界性心动过速")){
    		return "1320";
    	}else if(result.equals("交界性心动过缓")){
    		return "1330";
    	}else if(result.equals("室上性心律")){
    		return "1400";
    	}else if(result.equals("室上性心动过速")){
    		return "1420";
    	}else if(result.equals("室上性心动过缓")){
    		return "1430";
    	}else if(result.equals("偶发性室上性期前收缩")){
    		return "1470";
    	}else if(result.equals("频发性室上性期前收缩")){
    		return "1474";
    	}else if(result.equals("频发性室上性期前收缩形成二联律")){
    		return "1475";
    	}else if(result.equals("室性心律")){
    		return "1500";
    	}else if(result.equals("室性心动过速")){
    		return "1520";
    	}else if(result.equals("偶发性室性期前收缩")){
    		return "1570";
    	}else if(result.equals("频发性室性期前收缩")){
    		return "1574";
    	}else if(result.equals("频发性室性期前收缩形成二联律")){
    		return "1575";
    	}else if(result.equals("偶发性异位性期前收缩")){
    		return "1970";
    	}else if(result.equals("频发性异位性期前收缩")){
    		return "1974";
    	}else if(result.equals("频发性异位性期前收缩形成二联律")){
    		return "1975";
    	}else if(result.equals("AAI起搏心电图")){
    		return "16006";
    	}else if(result.equals("VVI起搏心电图")){
    		return "16007";
    	}else if(result.equals("无法判断的规则心律")){
    		return "1901";
    	}else if(result.equals("无法判断的心律")){
    		return "1902";
    	}else if(result.equals("PR间期缩短")){
    		return "2210";
    	}else if(result.equals("A型WPW综合症")){
    		return "2216";
    	}else if(result.equals("B型WPW综合症")){
    		return "2217";
    	}else if(result.equals("WPW综合症")){
    		return "2218";
    	}else if(result.equals("间歇性WPW综合症")){
    		return "2219";
    	}else if(result.equals("I度房室阻滞")){
    		return "2231";
    	}else if(result.equals("II度房室阻滞，莫氏I性")){
    		return "2232";
    	}else if(result.equals("II度房室阻滞，莫氏II性")){
    		return "2233";
    	}else if(result.equals("右室传导延迟")){
    		return "2420";
    	}else if(result.equals("不完全右束支传导阻滞")){
    		return "2440";
    	}else if(result.equals("完全右束支传导阻滞")){
    		return "2450";
    	}else if(result.equals("完全右束支传导阻滞合并右室肥厚")){
    		return "24501";
    	}else if(result.equals("不完全左束支传导阻滞")){
    		return "2540";
    	}else if(result.equals("完全左束支传导阻滞")){
    		return "2550";
    	}else if(result.equals("左前分支传导阻滞")){
    		return "2630";
    	}else if(result.equals("左后分支传导阻滞")){
    		return "2730";
    	}else if(result.equals("心室内传导延迟")){
    		return "2320";
    	}else if(result.equals("心室内传导阻滞")){
    		return "2330";
    	}else if(result.equals("** 正常心电图 **")){
    		return "9110";
    	}else if(result.equals("** 非典型心电图 **")){
    		return "9120";
    	}else if(result.equals("** 可疑心电图 **")){
    		return "9130";
    	}else if(result.equals("** 异常心电图 **")){
    		return "9140";
    	}else if(result.equals("可疑右心室肥厚")){
    		return "5120";
    	}else if(result.equals("右心室肥厚")){
    		return "5130";
    	}else if(result.equals("右心室肥厚，伴ST-T改变")){
    		return "5134";
    	}else if(result.equals("左心室肥厚待排")){
    		return "5211";
    	}else if(result.equals("左心室肥厚待排")){
    		return "5220";
    	}else if(result.equals("可疑左心室肥厚")){
    		return "5222";
    	}else if(result.equals("左心室肥厚")){
    		return "5233";
    	}else if(result.equals("左心室肥厚，伴ST-T改变")){
    		return "5234";
    	}else if(result.equals("陈旧性前壁心肌梗死待排")){
    		return "3113";
    	}else if(result.equals("亚急性前壁心肌梗死待排")){
    		return "3112";
    	}else if(result.equals("急性前壁心肌梗死待排")){
    		return "3111";
    	}else if(result.equals("可疑陈旧性前壁心肌梗死")){
    		return "3123";
    	}else if(result.equals("可疑亚急性前壁心肌梗死")){
    		return "3122";
    	}else if(result.equals("可疑急性前壁心肌梗死")){
    		return "3121";
    	}else if(result.equals("陈旧性前壁心肌梗死")){
    		return "3133";
    	}else if(result.equals("亚急性前壁心肌梗死")){
    		return "3132";
    	}else if(result.equals("急性前壁心肌梗死")){
    		return "3131";
    	}else if(result.equals("陈旧性前间壁心肌梗死待排")){
    		return "3413";
    	}else if(result.equals("亚急性前间壁心肌梗死待排")){
    		return "3412";
    	}else if(result.equals("急性前间壁心肌梗死待排")){
    		return "3411";
    	}else if(result.equals("可疑陈旧性前间壁心肌梗死")){
    		return "3423";
    	}else if(result.equals("可疑亚急性前间壁心肌梗死")){
    		return "3422";
    	}else if(result.equals("可疑急性前间壁心肌梗死")){
    		return "3421";
    	}else if(result.equals("陈旧性前间壁心肌梗死")){
    		return "3433";
    	}else if(result.equals("亚急性前间壁心肌梗死")){
    		return "3432";
    	}else if(result.equals("急性前间壁心肌梗死")){
    		return "3431";
    	}else if(result.equals("陈旧性侧壁心肌梗死待排")){
    		return "3513";
    	}else if(result.equals("亚急性侧壁心肌梗死待排")){
    		return "3512";
    	}else if(result.equals("急性侧壁心肌梗死待排")){
    		return "3511";
    	}else if(result.equals("可疑陈旧性侧壁心肌梗死")){
    		return "3523";
    	}else if(result.equals("可疑亚急性侧壁心肌梗死")){
    		return "3522";
    	}else if(result.equals("可疑急性侧壁心肌梗死")){
    		return "3521";
    	}else if(result.equals("陈旧性侧壁心肌梗死")){
    		return "3533";
    	}else if(result.equals("亚急性侧壁心肌梗死")){
    		return "3532";
    	}else if(result.equals("急性侧壁心肌梗死")){
    		return "3531";
    	}else if(result.equals("陈旧性下壁心肌梗死待排")){
    		return "3613";
    	}else if(result.equals("亚急性下壁心肌梗死待排")){
    		return "3612";
    	}else if(result.equals("急性下壁心肌梗死待排")){
    		return "3611";
    	}else if(result.equals("可疑陈旧性下壁心肌梗死")){
    		return "3623";
    	}else if(result.equals("可疑亚急性下壁心肌梗死")){
    		return "3622";
    	}else if(result.equals("可疑急性下壁心肌梗死")){
    		return "3621";
    	}else if(result.equals("陈旧性下壁心肌梗死")){
    		return "3633";
    	}else if(result.equals("亚急性下壁心肌梗死")){
    		return "3632";
    	}else if(result.equals("急性下壁心肌梗死")){
    		return "3631";
    	}else if(result.equals("陈旧性下壁及后壁心肌梗死待排")){
    		return "36132";
    	}else if(result.equals("亚急性下壁及后壁心肌梗死待排")){
    		return "36122";
    	}else if(result.equals("急性下壁及后壁心肌梗死待排")){
    		return "36112";
    	}else if(result.equals("可疑陈旧性下壁及后壁心肌梗死")){
    		return "36232";
    	}else if(result.equals("可疑亚急性下壁及后壁心肌梗死")){
    		return "36222";
    	}else if(result.equals("可疑急性下壁及后壁心肌梗死")){
    		return "36212";
    	}else if(result.equals("陈旧性下壁及后壁心肌梗死")){
    		return "36332";
    	}else if(result.equals("亚急性下壁及后壁心肌梗死")){
    		return "36322";
    	}else if(result.equals("急性下壁及后壁心肌梗死")){
    		return "36312";
    	}else if(result.equals("V3/V4异常Q波（心肌梗死的可能性待排）")){
    		return "31131";
    	}else if(result.equals("V1/V2异常Q波（心肌梗死的可能性待排）")){
    		return "34131";
    	}else if(result.equals("I/AVL/V5/V6异常Q波（心肌梗死的可能性待排）")){
    		return "35131";
    	}else if(result.equals("II/AVF异常Q波（心肌梗死的可能性待排）")){
    		return "36131";
    	}else if(result.equals("ST段抬高")){
    		return "4038";
    	}else if(result.equals("急性心包炎")){
    		return "40304";
    	}else if(result.equals("可疑前壁心肌缺血")){
    		return "4164";
    	}else if(result.equals("前壁心肌缺血")){
    		return "4165";
    	}else if(result.equals("可疑侧壁心肌缺血")){
    		return "4564";
    	}else if(result.equals("侧壁心肌缺血")){
    		return "4565";
    	}else if(result.equals("可疑下壁心肌缺血")){
    		return "4664";
    	}else if(result.equals("下壁心肌缺血")){
    		return "4665";
    	}else if(result.equals("可疑前壁心肌损伤")){
    		return "4136";
    	}else if(result.equals("前壁心肌损伤")){
    		return "4137";
    	}else if(result.equals("可疑侧壁心肌损伤")){
    		return "4536";
    	}else if(result.equals("侧壁心肌损伤")){
    		return "4537";
    	}else if(result.equals("可疑下壁心肌损伤")){
    		return "4636";
    	}else if(result.equals("下壁心肌损伤")){
    		return "4637";
    	}else if(result.equals("ST段降低，可能为洋地黄作用")){
    		return "40106";
    	}else if(result.equals("轻度的ST段低下")){
    		return "4011";
    	}else if(result.equals("轻度的ST段低下，可能为洋地黄作用")){
    		return "40116";
    	}else if(result.equals("中度的ST段低下")){
    		return "4012";
    	}else if(result.equals("中度的ST段低下，可能为洋地黄作用")){
    		return "40126";
    	}else if(result.equals("显著的ST段低下，可能为心肌损伤")){
    		return "4016";
    	}else if(result.equals("显著的ST段低下，可能为心肌损伤或洋地黄作用")){
    		return "40166";
    	}else if(result.equals("显著的ST段低下，符合心肌损伤")){
    		return "4017";
    	}else if(result.equals("ST段J点型降低，可能正常")){
    		return "4021";
    	}else if(result.equals("ST段J点型降低")){
    		return "4023";
    	}else if(result.equals("ST段抬高，可能为早期复极综合症")){
    		return "40302";
    	}else if(result.equals("早期复极综合症")){
    		return "40303";
    	}else if(result.equals("T波高耸，可能为高血钾症")){
    		return "4050";
    	}else if(result.equals("T波异常")){
    		return "4068";
    	}else if(result.equals("非特异性的T波异常，可能为洋地黄作用")){
    		return "40686";
    	}else if(result.equals("可疑前间壁心肌损伤")){
    		return "4436";
    	}else if(result.equals("前间壁心肌损伤")){
    		return "4437";
    	}else if(result.equals("可疑右心房肥厚")){
    		return "6120";
    	}else if(result.equals("右心房肥厚")){
    		return "6130";
    	}else if(result.equals("可疑左心房肥厚")){
    		return "6220";
    	}else if(result.equals("左心房肥厚")){
    		return "6230";
    	}else if(result.equals("电轴轻度右偏")){
    		return "7102";
    	}else if(result.equals("电轴右偏")){
    		return "7100";
    	}else if(result.equals("电轴轻度左偏")){
    		return "7202";
    	}else if(result.equals("电轴左偏")){
    		return "7200";
    	}else if(result.equals("电轴重度右偏")){
    		return "7300";
    	}else if(result.equals("S1-S2-S3综合症")){
    		return "7400";
    	}else if(result.equals("QRS-T夹角异常")){
    		return "7500";
    	}else if(result.equals("可疑慢性肺原性心脏病")){
    		return "8003";
    	}else if(result.equals("低电压")){
    		return "8100";
    	}else if(result.equals("肢导联低电压")){
    		return "8101";
    	}else if(result.equals("胸导联低电压")){
    		return "8102";
    	}else if(result.equals("右位心")){
    		return "8200";
    	}else if(result.equals("QTc间期延长")){
    		return "8304";
    	}else if(result.equals("QTc间期短缩")){
    		return "8305";
    	}else if(result.equals("左右上肢电极反接")){
    		return "101";
    	}else if(result.equals("ST-T异常")){
    		return "4048";
    	}else if(result.equals("DDD起搏心电图")){
    		return "16008";
    	}else if(result.equals("起搏心电图")){
    		return "16009";
    	}
    	return "-1";
	}
    
//    paramsMap.put("HeartRate", ecgParams[0]);
//	paramsMap.put("PAxis", ecgParams[1]);
//	paramsMap.put("QRSAxis", ecgParams[2]);
//	paramsMap.put("Taxis", ecgParams[3]);
//	paramsMap.put("PR", ecgParams[4]);
//	paramsMap.put("QRS", ecgParams[5]);
//	paramsMap.put("QT", ecgParams[6]);
//	paramsMap.put("QTc", ecgParams[7]);
//	paramsMap.put("RV5", ecgParams[8]);
//	paramsMap.put("SV1", ecgParams[9]);
//	paramsMap.put("RV5SV1", ecgParams[10]);
//	paramsMap.put("PExist", ecgParams[11]);
//	paramsMap.put("PWidth", ecgParams[12]);
    
    public static void main(String[] args){
    	
    	String xx = getCode("sdasdas");
    	System.out.println(xx);
    }
    
}
