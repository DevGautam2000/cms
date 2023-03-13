package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.Wallet;

public interface WalletRepo  extends JpaRepository<Wallet, Integer>{

}
