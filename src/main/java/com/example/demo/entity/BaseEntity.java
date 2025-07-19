package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

/**
 * エンティティ共通の作成・更新日時を保持する基底クラス。
 *
 * <p>すべてのエンティティに共通する監査情報（createdAt・updatedAt）を提供し、
 * 各エンティティでの重複定義を避けるために抽象化されている。</p>
 *
 * <p>@MappedSuperclass により、継承先エンティティにマッピングとして組み込まれる。</p>
 *
 * <p>新規登録時には @PrePersist によって自動でタイムスタンプが設定される。</p>
 */
@MappedSuperclass
public abstract class BaseEntity {

	/** 作成日時 */
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	/** 新規作成時に自動的に作成日・更新日をセットする。 */
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	/**@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	*/
}
