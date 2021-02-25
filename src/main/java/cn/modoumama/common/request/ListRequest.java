package cn.modoumama.common.request;

/**
 * 请求列表request
 * 
 * @author ouyangjian
 */
public class ListRequest extends Request {
	private static final long serialVersionUID = 8029099338063271641L;
	/** 第几页 */
	private Integer   page =1;
	/** 每页条数 */
	private Integer   rows = 10;
	/** 排序的属性*/
	private String   sort;
	/** 倒序desc  顺序 asc */
	private String   order;
	
	public Integer getPage() {
		return page;
	}
	
	public void setPage(Integer page) {
		if(page != null){
			this.page = page;
		}
	}
	
	public Integer getRows() {
		return rows;
	}
	
	public void setRows(Integer rows) {
		if(rows != null){
			this.rows = rows;
		}
	}
	
	public String getSort() {
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
}
