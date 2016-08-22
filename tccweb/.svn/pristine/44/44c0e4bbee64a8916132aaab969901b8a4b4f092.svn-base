
package com.tcci.tccweb.rs.tcc.model;

import com.tcci.tccweb.tcc.entity.News;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 用來傳遞多筆工廠資訊之用
 *
 * @author Jackson.Lee
 */
@XmlRootElement
public class NewsListDto {
    //工廠Dto List
    private List<News> newsList;

    public NewsListDto(){}
    
    public NewsListDto(List<News> newsList){
        this.newsList = newsList;
    }
    
    public List<News> getFactoryInfoDtoList() {
        return newsList;
    }

    public void setFactoryInfoDtoList(List<News> newsList) {
        this.newsList = newsList;
    }

    
}
