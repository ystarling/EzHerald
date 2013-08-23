用户账户模块
---------------

  - 其他模块进行用户账户获取的操作均在Authenticate.java文件中，通过该文件提供的Authenticate类的静态函数来获取
  - 获取账户的函数共有三个，若账户已登录，则返回该类型账户的UserAccount类型对象
  
Example
---------------
注意：这个例子是在Fragment中使用getIDcardUser函数，所以传入getSherlockActivity()。

UserAccount IDCardAccount = Authenticate.getIDcardUser(getSherlockActivity());
if(IDCardAccount==null){
***
}else {
***
}
