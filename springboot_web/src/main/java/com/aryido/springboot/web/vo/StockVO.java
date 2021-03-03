package com.aryido.springboot.web.vo;

<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

>>>>>>> branchExercise

/**
 * The stockVo value object.
 *
 * @author YunYang Lee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
<<<<<<< HEAD
@Builder
public class StockVO {
    private String stockSymbol;
    private String companyName;
    private float price;
    private int volume;
=======
public class StockVO implements Serializable {
	private String stockSymbol;
	private String companyName;
	private float price;
	private int volume;

>>>>>>> branchExercise

}
