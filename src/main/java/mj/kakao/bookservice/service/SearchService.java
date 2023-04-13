package mj.kakao.bookservice.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import mj.kakao.bookservice.model.entity.BookResultEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import mj.kakao.bookservice.model.dto.ResultDTO;

@Slf4j
@Service
public class SearchService {
    
    private final ApiService apiService;

    private ModelMapper modelMapper = new ModelMapper();

    public SearchService(ApiService apiService) {
        this.apiService = apiService;
    }


    public List<ResultDTO> retrieveBookList(String query) {
        try {
            List<BookResultEntity> l =  apiService.retrieveBookList(query);
            return l.stream().map(entity -> modelMapper.map(entity, ResultDTO.class)).collect(Collectors.toList());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
