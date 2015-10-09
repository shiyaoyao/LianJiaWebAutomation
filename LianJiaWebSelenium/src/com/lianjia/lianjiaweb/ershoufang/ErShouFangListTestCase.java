package com.lianjia.lianjiaweb.ershoufang;

import tasks.web.commons.LJNavTasks;
import tasks.web.ershoufang.ErShouFangListFilterTasks;
import tasks.web.homepage.LJHomeHeaderTasks;
import appobjects.web.Browser;

import com.lianjia.StringFetch;

public class ErShouFangListTestCase extends AbstractErShouFangListCategory{
	@Override
	public void testMain() throws Throwable {
		String cityName = StringFetch.getString("L_BEIJING");		
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		String curUrl = Browser.getCurrentUrl();
		
		//进入全部二手房列表页		
		assertEquals("进入二手房 - 全部二手房列表页", curUrl + "ershoufang/", LJNavTasks.gotoErShouFang());
		
		/*
		 * 区域
		 */
		//区域总数
		int quYuTotalNum = ErShouFangListFilterTasks.getAllQuYuNum();
		log.info(cityName + "全市共有: " + quYuTotalNum + "　个区域。");
		
		//随机选取某一区域
		int random = (int)(Math.random()*(quYuTotalNum-1) + 1);		
		ErShouFangListFilterTasks.clickQuYuByIndex(random);		
		String curQuYu = ErShouFangListFilterTasks.getQuYuNameByIndex(random);
		log.info("随机选取第" + random + "个区域："+ curQuYu);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curQuYu), "点击区域：" + curQuYu);
		
		/*
		 * 商圈
		 */
		//商圈总数
		int shangQuanTotalNum = ErShouFangListFilterTasks.getAllShangQuanNum();
		log.info(curQuYu + "区域下共有: " + shangQuanTotalNum + "　个商圈。");
		
		//随机选取某一商圈
		random = (int)(Math.random()*(shangQuanTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickShangQuanByIndex(random);		
		String curShangQuan = ErShouFangListFilterTasks.getShangQuanByIndex(random);
		log.info("随机选取第" + random + "个商圈："+ curShangQuan);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curShangQuan), "点击商圈：" + curShangQuan);
		
		/**
		 * 售价
		 */
		//售价总数
		int shouJiaTotalNum = ErShouFangListFilterTasks.getAllShouJiaNum();
		log.info(curShangQuan + "商圈下共有: " + shouJiaTotalNum + "　个售价分类。");
		
		//随机选取某一售价分类
		random = (int)(Math.random()*(shouJiaTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickShouJiaByIndex(random);		
		String curShouJia = ErShouFangListFilterTasks.getShouJiaByIndex(random);
		log.info("随机选取第" + random + "个售价分类："+ curShouJia);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curShouJia), "点击售价：" + curShouJia);
		
		/**
		 * 面积
		 */
		//面积总数
		int mianJiTotalNum = ErShouFangListFilterTasks.getAllMianJiNum();
		log.info(curShangQuan + "商圈下共有: " + mianJiTotalNum + "　个面积分类。");
		
		//随机选取某一售价分类
		random = (int)(Math.random()*(mianJiTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickMianJiByIndex(random);		
		String curMianJi = ErShouFangListFilterTasks.getMianJiByIndex(random);
		log.info("随机选取第" + random + "个面积分类："+ curMianJi);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curMianJi), "点击面积：" + curMianJi);
		
		/**
		 * 房型
		 */
		//房型分类总数
		int fangXingTotalNum = ErShouFangListFilterTasks.getAllFangXingNum();
		log.info(curShangQuan + "商圈下共有: " + fangXingTotalNum + "　个房型分类。");
		
		//随机选取某一房型
		random = (int)(Math.random()*(fangXingTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickFangXingByIndex(random);		
		String curFangXing = ErShouFangListFilterTasks.getFangXingByIndex(random);
		log.info("随机选取第" + random + "个房型分类："+ curFangXing);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curFangXing), "点击房型：" + curFangXing);
		
		/**
		 * 筛选
		 */
		//朝向分类总数
		int chaoXiangTotalNum = ErShouFangListFilterTasks.getAllChaoXiangNum();
		log.info(curShangQuan + "商圈下共有: " + chaoXiangTotalNum + "　个朝向分类。");
		
		//随机选取某一朝向
		random = (int)(Math.random()*(chaoXiangTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickChaoXiangByIndex(random);		
		String curChaoXiang = ErShouFangListFilterTasks.getChaoXiangByIndex(random);
		log.info("随机选取第" + random + "个朝向分类："+ curChaoXiang);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curChaoXiang), "点击朝向：" + curChaoXiang);
		
		//楼龄分类总数
		int louLingTotalNum = ErShouFangListFilterTasks.getAllLouLingNum();
		log.info(curShangQuan + "商圈下共有: " + louLingTotalNum + "　个楼龄分类。");
		
		//随机选取某一楼龄
		random = (int)(Math.random()*(louLingTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickLouLingByIndex(random);		
		String curLouLing = ErShouFangListFilterTasks.getLouLingByIndex(random);
		log.info("随机选取第" + random + "个楼龄分类："+ curLouLing);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curLouLing), "点击楼龄：" + curLouLing);
		
		//楼层分类总数
		int louCengTotalNum = ErShouFangListFilterTasks.getAllLouCengNum();
		log.info(curShangQuan + "商圈下共有: " + louCengTotalNum + "　个楼层分类。");
		
		//随机选取某一楼层
		random = (int)(Math.random()*(louCengTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickLouCengByIndex(random);		
		String curLouCeng = ErShouFangListFilterTasks.getLouCengByIndex(random);
		log.info("随机选取第" + random + "个楼层分类："+ curLouCeng);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curLouCeng), "点击楼层：" + curLouCeng);
		
		//标签分类总数
		int biaoQianTotalNum = ErShouFangListFilterTasks.getAllBiaoQianNum();
		log.info(curShangQuan + "商圈下共有: " + biaoQianTotalNum + "　个标签分类。");
		
		//随机选取某一楼层
		random = (int)(Math.random()*(biaoQianTotalNum-1) + 1);
		ErShouFangListFilterTasks.clickBiaoQianByIndex(random);		
		String curBiaoQian = ErShouFangListFilterTasks.getBiaoQianByIndex(random);
		log.info("随机选取第" + random + "个标签分类："+ curBiaoQian);
		
		logCompare(true, ErShouFangListFilterTasks.isOptionSelected(curBiaoQian), "点击标签：" + curBiaoQian);
		
		logCompare(true, ErShouFangListFilterTasks.clickFilterEmpty(), "清空所有筛选项");
	}
}
