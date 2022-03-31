package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(indexes = @Index(columnList = "step"))
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LRP extends Rectangular{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long step;
}
