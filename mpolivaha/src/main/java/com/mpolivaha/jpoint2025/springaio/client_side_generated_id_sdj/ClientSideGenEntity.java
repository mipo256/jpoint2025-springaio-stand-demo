package com.mpolivaha.jpoint2025.springaio.client_side_generated_id_sdj;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSideGenEntity implements Persistable<UUID> {

	@Id
	private UUID id;

	private String name;

	@Transient
	private boolean isNew;

	public static ClientSideGenEntity forInsert(String name) {
		return new ClientSideGenEntity(UUID.randomUUID(), name, true);
	}

	public static ClientSideGenEntity forUpdate(UUID id, String name) {
		return new ClientSideGenEntity(id, name, false);
	}

	@NotNull
	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
}
