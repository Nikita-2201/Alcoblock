package ru.gknsv.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;

@Data
@Entity
@Table(name = "history")
public class History {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "alco_id")
	private String alcoId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;

	private String volume;
}
