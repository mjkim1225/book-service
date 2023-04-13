package mj.kakao.bookservice.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
public class BookResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
//    private List<String> authors;
//    private LocalDateTime datetime;
//    private String isbn;
//    private int price;
//    private String publisher;
//    private int salePrice;
//    private String status;
//    private String thumbnail;
//    private List<String> translators;
//    private String url;
}
