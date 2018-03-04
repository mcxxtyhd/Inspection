package com.inspect.test;

import java.util.List;

import org.apache.poi.ss.util.CellReference;

import com.inspect.model.basis.TSummaryConfig;

public class 列表修改 {
	public void getSConfig1(int flag) {
		// TODO Auto-generated method stub
		
/*		List<TSummaryConfig> sconfigList=baseDao.find("from TSummaryConfig where flag="+flag);
		Eoip p=new Eoip();
		List<TSummaryConfig> dd=p.testUpdateExcelPosition(sconfigList);
		if(dd!=null&&dd.size()>0){
			for(TSummaryConfig config:dd){
				baseDao.save(config);
			}
		}*/
		
	}
	/**
	 * 业务逻辑：
	 * 获取数据库数据集
	 * 循环
	 * 取得excel坐标
	 * 利用CellReference对象获取行、列的数值
	 * 改变列值：col = col + 4
	 * 获取新的excel坐标字符串，formatstring方法
	 * basedao，更新坐标
	 * 
	 * 		 
	 * */
	
public List<TSummaryConfig> testUpdateExcelPosition(List<TSummaryConfig> list){
		for(int i=0;i<list.size();i++){
			TSummaryConfig config=list.get(i);
			//核心代码：
			CellReference cr = new CellReference(config.getScell().trim());
			int col = cr.getCol();
			String colstr = new String();
			
				colstr = cr.convertNumToColString(col+4);
			String ep2 = colstr+"1";
			config.setScell(ep2);
		}
		return list;
		//baseDao.save(entity);
		}
		
public static void main(String[] args) {

	

}
}
