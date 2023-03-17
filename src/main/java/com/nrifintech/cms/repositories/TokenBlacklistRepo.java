package com.nrifintech.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrifintech.cms.entities.TokenBlacklist;

public interface TokenBlacklistRepo extends JpaRepository<TokenBlacklist,String>{
    
}
