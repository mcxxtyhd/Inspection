package com.inspect.util.excel;

import java.util.Map;


/**
 * Eoiprule类用于配合Eoip工具类进行excle导入导出的规则设置.<br>
 * 所有参数都是基于0基数的。<br>
 * @author wangxy
 */
public class Eoiprule {

	//导入模式数据开始

	private short sheetnumber=0; //which sheet data should be treated,default 0;第几个sheet读取
	private short rowheaderpos;//row header position,which row? 
	private short rowcontentspos;	//start row position of data contents//从第几行开始读取内容
	private short colheaderpos;	//column header position, which column
	private short colcontentspos;	//start column position of data contents
	private String nametocol;		//format: "name,0;age,1;address,4;email,7;"导出的位置
	private String validaterule;	//validaterule; "1,NotNull;2,Exeption-Exit;3,Exeption-Abandon;4,0;"
									//制定导出异常规则，比如某个位置不能为空，设置默认值，异常处理等等
	
	private int maxcolnum;		//the maxium number of columns;好像没想象中有用，暂时没用
	
	/*
	 * 针对导入时是名称，存储时需映射为另一张关联表的id的情况
	 * 名称与id的映射关系；
	 * 如"6,com.inspect.model.monitor.TReport:name:id;"
	 * 表示第7列字段的内容为com.inspect.model.monitor.TReport中name的值，导入时应将其对应的id值赋予entity
	 * 默认id的数据类型为int 或Integer；name的类型为String
	 * */
	private String	name2id;	
	//导出模式数据开始
	
	public String getName2id() {
		return name2id;
	}
	/**导入参数	名称与id的映射关系
	 * 针对导入时是名称，存储时需映射为另一张关联表的id的情况
	 * 需要有列值、表名、源字段、目的字段
	 * 如"6,com.inspect.model.monitor.TReport:name:id;"
	 * 表示第7列字段的内容为com.inspect.model.monitor.TReport中name的值，导入时应将其对应的id值赋予entity
	 * 默认id的数据类型为int 或Integer；name的类型为String
	 * 该功能还没有实现
	 * */
	public void setName2id(String name2id) {
		this.name2id = name2id;
	}
	//导出的模式，重要！！！ true为绝对模式，可直接指定字段值到cell的映射；false为列模式，指定字段到列的映射
	private Boolean expmode = false;
	
	//导出的字段名与excel位置的对应关系，如："xequtnum,b3;xequid,b4"
	private String expd2e;
	
	private String expfilename;
	

	private String exptemplatefile;
	
	//开始写入的起始列，对列模式有效；绝对模式无效；
	private int	expstartcol;
	
	private Map<String,String> expenumstr;
	
	private String exppostfixrule;
	
	private int expnumsheetperwb = 1;	//定义一个excel文件中sheet 的个数
	
	private Map<String,String> setdemap;
	
	private String setfieldvalue; 


	private String  setfieldmap; //绝对模式字段所在位置


	private String specialFields;
	
	public String getSpecialFields() {
		return specialFields;
	}
	/**导出参数	特殊处理的字段规则
	 * 需要特殊处理的字段和excel单元格的映射关系
	 * excel位置请用大写
	 * cell位置-字段名的值对
	 * 例如："C7,xequtnum;"
	 * 注意：特殊规则的生效需要改写Eoip中的specialFieldprocess方法，在方法中实现特殊规则
	 */
	public void setSpecialFields(String specialFields) {
		this.specialFields = specialFields;
	}
	public String getSetfieldmap() {
		return setfieldmap;
	}
	/**导出参数	与setfieldvalue配合使用
	 * 值与excel位置的映射关系
	 * 表示该值对应的excel位置，类似expd2e，但与之又有区别；
	 * 这里的名称，都是setfieldvalue中匹配字段（第一个字段）在数据库中的值
	 * 例如："馈线窗处密封情况良好,b10;馈线接地线固定符合规范 ,b11;馈线窗处接地系统符合安装规范,b12"
	 */
	public void setSetfieldmap(String setfieldmap) {
		this.setfieldmap = setfieldmap;
	}
	
	
	
	/* 导出规则设置
	 	//测试数据初始化
		for(int i=0;i<9;i++){
		  EquipmentVo vo = new EquipmentVo();
		  if(i/2 ==0)
			  vo.setBeqcount("ooo");
		  else
			  vo.setBeqcount("yyy");
		  vo.setPointid(i);
		  vo.setEposx(23.22+i);
		  vo.setEaddress(i+ "address");
		
		  db.add(vo);
		}
	//导出规则设置
	 Eoiprule rule = new Eoiprule();
	 rule.setExpd2e("beqcount,B3;pointid,c2;eposx,d5;eaddress,a2");
	 rule.setExpfilename("E:\\temp\\testpoi\\equipment_");
	 rule.setExptemplatefile("E:\\temp\\testpoi\\equipment_template_abs.xls");
	 rule.setExpstartcol(3);

	 //枚举型数据设置
	 Map<String,String> enummap = new HashMap<String,String>();
	 enummap.put("pointid", "0,abc;1,def;2,ghk");
	 enummap.put("beqcount", "yyy,□是    □否;ooo,女");
	 
	 rule.setExpenumstr(enummap);
	 
	 Boolean mode = true;
	 rule.setExpmode(mode);
	 rule.setExppostfixrule("name,eaddress");
	 rule.setExpnumsheetperwb(3); //
	 
	 */
	

	public String getSetfieldvalue() {
		return setfieldvalue;
	}
	/**导出参数	与setdemap参数配合使用
	 * 该数据需要认真理解
	 * 泛型属性对应的entity字段间名称与值的映射关系
	 * 为了简化设计和实现，暂时只支持一个字段映射，即只有一个名称-名称映射
	 * 第一个名称为匹配字段，该字段的值将在setfieldvalue中的名称进行查找
	 * 第二个名称为输出值字段
	 * 例如："xproname,xvalue"
	 * 表示如果xproname字段的值在setfieldvalue中有定义，则向excel文件的单元格中填充xvalue字段对应的值
	 */
	public void setSetfieldvalue(String setfieldvalue) {
		this.setfieldvalue = setfieldvalue;
	}
	public Map<String, String> getSetdemap() {
		return setdemap;
	}
	/**导出参数	集合类型属性的映射关系定义
	 * 对要导出的entity中包含有泛型类型的属性进行定义
	 * 由类名称，字段名组成；建立两者间的联系
	 * 例如："com.inspect.model.monitor.TInspectItemDetailReport", "inspectreportdetailmsgs"
	 */
	public void setSetdemap(Map<String, String> setdemap) {
		this.setdemap = setdemap;
	}


	public String getNametocol() {
		return nametocol;
	}
	/**导入参数	数据表字段名称与列值的映射关系	必选参数	*/
	public void setNametocol(String nametocol) {
		this.nametocol = nametocol;
	}
	public short getSheetnumber() {
		return sheetnumber;
	}
	/**导入参数	sheet的编号，默认为0 */
	public void setSheetnumber(short sheetnumber) {
		this.sheetnumber = sheetnumber;
	}
	public short getRowheaderpos() {
		return rowheaderpos;
	}
	/**导入参数	行标题的行号	未用	*/
	public void setRowheaderpos(short rowheaderpos) {
		this.rowheaderpos = rowheaderpos;
	}
	public short getRowcontentspos() {
		return rowcontentspos;
	}
	/**导入参数	数据行的行号	*/
	public void setRowcontentspos(short rowcontentspos) {
		this.rowcontentspos = rowcontentspos;
	}
	public short getColheaderpos() {
		return colheaderpos;
	}
	/**导入参数	列标题的列号	未用	*/
	public void setColheaderpos(short colheaderpos) {
		this.colheaderpos = colheaderpos;
	}
	public short getColcontentspos() {
		return colcontentspos;
	}
	/**列数据的列号	未用	*/
	public void setColcontentspos(short colcontentspos) {
		this.colcontentspos = colcontentspos;
	}

	public String getValidaterule() {
		return validaterule;
	}
	/**导入参数	数据验证规则	可选
	 * 列值-规则名的值对，对异常情况的处理规则 <br>
	 * 同一列数据，可同时配置多条数据验证规则，其规则名的优先级自高到低解释如下：<br>
	 * NotNull：	该列的值不能为空；否则本条数据被抛弃<br>
	 * Exception-Exit：	该列的值如果类型转换失败，则本次转换异常退出<br>
	 * Exception-Abandon：	该列的值如果类型转换失败，则本条数据被抛弃<br>
	 * 字符串值：	该列的值如果类型转换失败，则用本字符串值作为默认值代替本字段；<br>
	 * 
	 * */
	public void setValidaterule(String validaterule) {
		this.validaterule = validaterule;
	}
	public int getMaxcolnum() {
		return maxcolnum;
	}
	/**导入参数	最大的列值	未用	*/
	public void setMaxcolnum(int maxcolnum) {
		this.maxcolnum = maxcolnum;
	}
	public String getExpd2e() {
		return expd2e;
	}
	/**导出参数	导出的字段名与excel位置的对应关系<br>
	 * 字段名-excel坐标的映射
	 * excel位置由excel单元格坐标表示，如A5,建议字符大写<br>
	 * 对于列模式，坐标中的数字（行号）无意义，但必须要有，如A0
	 * 例如："xequtnum,B3;xequid,C4"
	 * */
	public void setExpd2e(String expd2e) {
		this.expd2e = expd2e;
	}
	public String getExpfilename() {
		return expfilename;
	}
	/**导出参数 导出文件的文件名<br>
	 * 对于绝对模式：<br>
	 *导出的文件名前缀，完整的文件名将由该前缀和起始、结束记录数字拼成<br>
	 *例如：<br>
	 *前缀为："E:\\temp\\testpoi\\equipment_"<br>
	 *本次起止的记录数为0和19，共20条记录<br>
	 *则最终的文件名为：E:\\temp\\testpoi\\equipment_0_19.xls<br>
	 *对于列模式：<br>
	 * 导出的文件名，应为完整的文件名称，如：E:\\temp\\testpoi\\equipment.xls<br>
	 */
	public void setExpfilename(String expfilename) {
		this.expfilename = expfilename;
	}
	public String getExptemplatefile() {
		return exptemplatefile;
	}
	/**导出参数	导出需要的excel模板文件名，绝对路径
	 * 对于绝对模式，模板只能保留有一个sheet，其他的都应删掉；否则会出错
	 */
	public void setExptemplatefile(String exptemplatefile) {
		this.exptemplatefile = exptemplatefile;
	}
	public int getExpstartcol() {
		return expstartcol;
	}
	/**导出参数	数据开始写入的起始列
	 * 仅对列模式有效；
	 */
	public void setExpstartcol(int expstartcol) {
		this.expstartcol = expstartcol;
	}
	public Map<String, String> getExpenumstr() {
		return expenumstr;
	}
	/**导出参数	枚举类型数据的替换规则
	 * 由字段名，和名称值对组成
	 * 例如： "beqcount", "yyy,□是    □否;ooo,女"
	 * 表示，对于beqcount字段，如值为yyy，则导出的值为□是    □否；如值为ooo，导出值为女
	 */
	public void setExpenumstr(Map<String, String> expenumstr) {
		this.expenumstr = expenumstr;
	}
	public Boolean getExpmode() {
		return expmode;
	}
	/**导出参数	导出模式：
	 * true，绝对模式；false，列模式；默认为列模式
	 * */
	public void setExpmode(Boolean expmode) {
		this.expmode = expmode;
	}
	public String getExppostfixrule() {
		return exppostfixrule;
	}
	/**导出参数	sheet的名称命名规则
	 * 仅绝对模式下有效；
	 * 例如："name,xequtnum"，表示用字段“xequtnum”的值作为名称；
	 * 当前仅支持"name"一种模式
	 */
	public void setExppostfixrule(String exppostfixrule) {
		this.exppostfixrule = exppostfixrule;
	}
	public int getExpnumsheetperwb() {
		return expnumsheetperwb;
	}
	/**导出参数	每个excel文件中包含多少个sheet
	 * 仅绝对模式下有效；默认值为1
	 * 表示每个excel文件包含多少个sheet，即一个excel文件包含多少条记录后重新产生新文件
	 */
	public void setExpnumsheetperwb(int expnumsheetperwb) {
		this.expnumsheetperwb = expnumsheetperwb;
	}
	
}

