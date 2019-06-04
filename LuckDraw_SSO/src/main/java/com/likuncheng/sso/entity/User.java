package com.likuncheng.sso.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.likuncheng.common.CreateAccountNumber;

import lombok.Data;

@Data
public class User {
	
	// 数据库唯一标识
	private Integer id;
	// 用户账号
	private String accountNumber;
	// 密码
	private String passWord;
	// 昵称
	private String nickName;
	// 注册时间
	private String registerTime;
	//版本号
	private Integer version;
	
	private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 0);

	public User() {
		//第一次应该是1
		Integer reference = atomicStampedReference.getReference();
		atomicStampedReference.compareAndSet(reference, reference+1, atomicStampedReference.getStamp(), atomicStampedReference.getStamp()+1);
		this.accountNumber = CreateAccountNumber.createAccountNumber();
		// 初始化昵称为账号
		this.nickName = this.accountNumber;
		// 初始化创建时间
		this.registerTime = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
	}
}
