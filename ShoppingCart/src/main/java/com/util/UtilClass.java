package com.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.entity.CategoryEntity;
import com.entity.ImageEntity;
import com.entity.MemberDtlsEntity;
import com.entity.ProductDtlsEntity;
import com.model.LoginResponseBean;
import com.model.RestRequestBean;

public class UtilClass {
	Configuration cfg=new AnnotationConfiguration().configure();
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
	
	public List<CategoryEntity> getCategoryList() throws Exception{		
		cfg.addAnnotatedClass(CategoryEntity.class);
		Session session=cfg.buildSessionFactory().openSession();
		Query query=session.createQuery("from CategoryEntity");
		List<CategoryEntity> list=query.list();				
		return list;		
	}
	
	public MemberDtlsEntity getMemberDtls(String memberId) throws Exception{	
		cfg.addAnnotatedClass(MemberDtlsEntity.class);
		Session session=cfg.buildSessionFactory().openSession();
		Query query=session.createQuery("from MemberDtlsEntity where member_id = :member_id");
		query.setString("member_id", memberId);
		List<MemberDtlsEntity> list=query.list();				
		if(list.isEmpty()){
			list.add(new MemberDtlsEntity());
		}		
		return list.get(0);		
	}
	
	public ProductDtlsEntity getProductDtls(String productId) throws Exception{		
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select * from product where product_id = ?");
		pst.setString(1, productId);		
		ResultSet rs=pst.executeQuery();
		ProductDtlsEntity entity=new ProductDtlsEntity();
		while(rs.next()){			
			entity.setCategoryId(rs.getString("CATEGORY_ID"));
			entity.setPrice(rs.getDouble("PRICE"));
			entity.setProductId(rs.getString("PRODUCT_ID"));
			entity.setProductName(rs.getString("PRODUCT_NAME"));
			entity.setSellerId(rs.getString("SELLER_ID"));
		}
		return entity;		
	}
	
	
	public LoginResponseBean validateUser(RestRequestBean bean) throws Exception{		
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select * from login_dtls where member_id = ?");
		pst.setString(1, bean.getMemberId());		
		LoginResponseBean responseBean=new LoginResponseBean();
		ResultSet rs=pst.executeQuery();
		if(rs.next()){			
			PreparedStatement pst1=con.prepareStatement("select * from login_dtls where member_id = ? and password=?");
			pst1.setString(1, bean.getMemberId());		
			pst1.setString(2, bean.getPassword());	
			ResultSet rs1=pst1.executeQuery();
			if(rs1.next()){
				responseBean.setMemberId(rs1.getString("MEMBER_ID"));
				responseBean.setPassword(rs1.getString("PASSWORD"));
				responseBean.setRole(rs1.getString("ROLE"));
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
		return responseBean;
		
	}
	
	public ArrayList getProductIdListByCategory(RestRequestBean bean) throws Exception{
		ArrayList dataList=new ArrayList();
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select PRODUCT_ID FROM product where CATEGORY_ID=?");
		pst.setString(1, bean.getCategoryId());		
		ResultSet rs=pst.executeQuery();
		while(rs.next()){
			dataList.add(rs.getString(1));
		}
		return dataList;
	}
	
	public ArrayList getProductIdListByName(RestRequestBean bean) throws Exception{
		ArrayList dataList=new ArrayList();
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select PRODUCT_ID FROM product where PRODUCT_NAME like '%"+bean.getProductName()+"%'");		
		ResultSet rs=pst.executeQuery();
		while(rs.next()){
			dataList.add(rs.getString(1));
		}
		return dataList;
	}
	
	public ArrayList getImageIdListByProduct(RestRequestBean bean) throws Exception{
		ArrayList dataList=new ArrayList();
		Connection con=getConnection();
		PreparedStatement pst=con.prepareStatement("select IMAGE_ID FROM image_tab where PRODUCT_ID=?");
		pst.setString(1, bean.getProductId());		
		ResultSet rs=pst.executeQuery();
		while(rs.next()){
			dataList.add(rs.getString(1));
		}
		return dataList;
	}
		
	public Connection getConnection() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con =DriverManager.getConnection("jdbc:mysql://localhost/shoppingcart","root","root123"); 
		return con;
	}

}
