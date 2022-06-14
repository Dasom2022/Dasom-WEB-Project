package com.dama.repository;

import com.dama.model.entity.Member;
import com.dama.model.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.username = ?1, m.email = ?2, m.imgUrl = ?3 WHERE m.socialId = ?4 AND m.socialType = ?5")
    void updateUserBySocialIdAndSocialType(String username, String email, String imgURL, String socialId, SocialType socialType);

    Optional<Member> findByPassword(String password);

    Optional<Member> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByUsernameAndPhoneNumber(String username,String phoneNumber);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    Optional<Member> findByNickname(String nickname);
}
