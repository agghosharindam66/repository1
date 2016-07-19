package com.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.entity.CategoryEntity;
import com.entity.ImageEntity;
import com.entity.LoginDtlsEntity;
import com.entity.MemberDtlsEntity;
import com.entity.ProductDtlsEntity;
import com.model.LoginResponseBean;
import com.model.RestRequestBean;

public class UtilClass {	
	public byte[] readImage(RestRequestBean bean) throws Exception{
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select IMAGE from image_tab where product_id=? and image_id=?");
		pst.setString(1, bean.getProductId());
		pst.setString(2, bean.getImageId());
		ResultSet rs=pst.executeQuery();
		byte[] image=null;
		while (rs.next()) {			
			image=rs.getBlob("IMAGE").getBytes(1, (int)rs.getBlob("IMAGE").length());
		}
		return image;
	}
	
	@SuppressWarnings("unchecked")
	public List<CategoryEntity> getCategoryList() throws Exception{		
		Session session=com.util.Configuration.getSession(CategoryEntity.class);
		Query query=session.createQuery("from CategoryEntity");		
		List<CategoryEntity> list=query.list();		
		session.close();
		return list;		
	}
	
	@SuppressWarnings("unchecked")
	public MemberDtlsEntity getMemberDtls(String memberId) throws Exception{	
		Session session=com.util.Configuration.getSession(MemberDtlsEntity.class);
		Query query=session.createQuery("from MemberDtlsEntity where memberId = :member_id");
		query.setString("member_id", memberId);
		List<MemberDtlsEntity> list=query.list();				
		if(list.isEmpty()){
			list.add(new MemberDtlsEntity());
		}	
		session.close();
		return list.get(0);		
	}
	
	@SuppressWarnings("unchecked")
	public ProductDtlsEntity getProductDtls(String productId) throws Exception{		
		Session session=com.util.Configuration.getSession(ProductDtlsEntity.class);
		Query query=session.createQuery("from ProductDtlsEntity where productId = :product_id");
		query.setString("product_id", productId);
		List<ProductDtlsEntity> list=query.list();				
		if(list.isEmpty()){
			list.add(new ProductDtlsEntity());
		}		
		session.close();
		return list.get(0);			
	}
		
	@SuppressWarnings("unchecked")
	public LoginResponseBean validateUser(RestRequestBean bean) throws Exception{		
		LoginResponseBean responseBean=new LoginResponseBean();
		Session session=com.util.Configuration.getSession(LoginDtlsEntity.class);
		LoginDtlsEntity entity=(LoginDtlsEntity)session.get(LoginDtlsEntity.class, bean.getMemberId());		
		if(entity!=null && !"".equalsIgnoreCase(entity.getMemberId())){
			Query query=session.createQuery("from LoginDtlsEntity where memberId = :memberId and password= :password");
			query.setString("memberId", bean.getMemberId());
			query.setString("password", bean.getPassword());
			List<LoginDtlsEntity> list=query.list();
			if(!list.isEmpty()){
				entity=list.get(0);
				responseBean.setMemberId(entity.getMemberId());
				responseBean.setPassword(entity.getPassword());
				responseBean.setRole(entity.getRole());
				responseBean.setErrorCode("0");
				responseBean.setErrorMsg("");
			}
			else{
				responseBean.setErrorCode("-2");
				responseBean.setErrorMsg("Invalid password.");
			}					
		}
		else{
			responseBean.setErrorCode("-1");
			responseBean.setErrorMsg("Invalid Login Id.");
		}				
		session.close();
		return responseBean;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getProductIdListByCategory(RestRequestBean bean) throws Exception{
		Session session=com.util.Configuration.getSession(ProductDtlsEntity.class);
		Query query=session.createQuery("select e.productId from ProductDtlsEntity e where e.categoryId=:categoryId");
		query.setString("categoryId", bean.getCategoryId());
		ArrayList<String> dataList=(ArrayList<String>)query.list();
		session.close();		
		return dataList;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getProductIdListByName(RestRequestBean bean) throws Exception{		
		Session session=com.util.Configuration.getSession(ProductDtlsEntity.class);
		Query query=session.createQuery("select t.productId from ProductDtlsEntity t where t.productName like :productName");
		query.setString("productName", bean.getProductName());
		ArrayList<String> dataList=(ArrayList<String>)query.list();		
		return dataList;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getImageIdListByProduct(RestRequestBean bean) throws Exception{
		Session session=com.util.Configuration.getSession(ImageEntity.class);
		Query query=session.createQuery("select e.imageId from ImageEntity e where e.productId=:productId");
		query.setString("productId", bean.getProductId());
		ArrayList<String> dataList=(ArrayList<String>)query.list();
		session.close();
		return dataList;
	}
		
	public Connection getConnection() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con =DriverManager.getConnection("jdbc:mysql://localhost/shoppingcart","root","root123"); 
		return con;
	}

}
