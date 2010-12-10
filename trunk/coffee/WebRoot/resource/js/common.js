	
	/**
	 * 删除记录 
	 * @param {Object} url
	 */
	function delete_(url){
		if(confirm("确定删改该记录?")){
			window.location.href = url;
		}
		return false;
	}