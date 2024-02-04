package ru.gknsv.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "users")
public class User {

	public static final String SEX_MALE = "male";
	public static final String SEX_FEMALE = "female";

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String login;
	private String password;
	private String sex;
	private double height;
	private double weight;
	private double experience;
}
