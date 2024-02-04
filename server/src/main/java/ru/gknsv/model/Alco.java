package ru.gknsv.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "alco")
public class Alco {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String title;
	private double strength;

	@Column(name = "picture_id")
	private String pictureId;

	@Column(name = "user_id")
	private String userId;
}
