#namespace("user")
  #sql("findUser")
  SELECT * FROM T_USER t
  #end

  ### 定义模板函数 deleteByIdList
  #define deleteByIdList(table,idList)
  delete from #(table) where id in(
    #for (id : idList)
      #(for.index > 0 ? ", " : "") #(id)
    #end
  )

  ### 调用sql模板
  #sql("deleteUserByIdList")
    #@deleteByIdList("t_user",idList)
    #end
  #end
#end