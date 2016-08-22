/*
* RESTful Service: 台灣水泥新聞訊息
*/
package com.tcci.tccweb.rs.tcc;

import com.tcci.tccweb.tcc.entity.News;
import com.tcci.tccweb.tcc.facade.NewsFacade;
import com.tcci.tccweb.rs.tcc.model.NewsDto;
import com.tcci.tccweb.rs.util.ResponseUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  台泥官網-新聞訊息
 * @author Jackson Lee
 */
@Singleton
@Path("tccNewsService")
public class NewsREST {
    private final static Logger logger = LoggerFactory.getLogger(NewsREST.class);
    
    //<editor-fold defaultstate="collapsed" desc="Inject">
    @Inject private NewsFacade newsFacade;
    
    //</editor-fold>
    /**
     * 列出所有已發佈的新聞資料(enable=1)
     * 例:
     * http://localhost:8080/tccweb/service/newsService/listAllReleasedNews?fromYY=2016&toYY=2016&orderBy=1
     * @param fromYY 開始年度(含)
     * @param toYY 結束年度(含)
     * @param orderBy 排序方式(1: 正序，依idnum小到大) → 預設近期新聞在前(大到小)
     * @return
     */
    @GET
    @Path("listAllReleasedNews")
    @Produces({MediaType.APPLICATION_JSON})
    public Response listAllReleasedNews(
            @QueryParam("fromYY") String fromYY,
            @QueryParam("toYY") String toYY,
            @QueryParam("orderBy") int orderBy) {
        
        List<News> results = newsFacade.findReleasedNewsByYY(fromYY, toYY, orderBy);
        List<NewsDto> newsDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(results)) {
            for (News news: results){
                NewsDto dto = createNewsDto(news);
                newsDtoList.add(dto);
            }
        }
        
        return ResponseUtil.createJsonResponse(newsDtoList);
    }
    
    
    /**
     * 依id取得已發佈的新聞內容(enable=1)
     * 例:
     * http://localhost:8080/tccweb/service/newsService/findNewsById?newsId=10
     * @param newsId 新聞訊息id，為正整數
     * @return
     */
    @GET
    @Path("findNewsById")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findNewsById(
            @QueryParam("newsId") String newsId) {
        if (StringUtils.isBlank(newsId)) {
//            String message = "Param: newsId is blank!";
//            return ResponseUtil.createJsonResponse(new ResponseMessageDto(MessageCode.ERROR_LEVEL_SERVER, message));
return ResponseUtil.createJsonResponse(null);
        }
        try{
            List<News> results = newsFacade.findById(Integer.valueOf(newsId));
            List<NewsDto> newsDtoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(results)) {
                for (News news: results){
                    NewsDto dto = createNewsDto(news);
                    newsDtoList.add(dto);
                }
            }
            
            return ResponseUtil.createJsonResponse(newsDtoList);
        }catch (Exception e){
            return ResponseUtil.createJsonResponse(null);
        }
    }
    
    /**
     *  Convert to NewsDto
     * @param news
     * @return 
     */
    private NewsDto createNewsDto(News news){
        NewsDto dto = new NewsDto();
        dto.setIdnum(news.getIdnum());
        dto.setYy(news.getYy());
        
        dto.setSubject(news.getSubject());
        dto.setSubjectCHS(news.getSubjectCHS());
        dto.setSubjecten(news.getSubjecten());
        
        dto.setMemo(news.getMemo());
        dto.setMemoCHS(news.getMemoCHS());
        dto.setMemoen(news.getMemoen());
        
        return dto;
    }
    
}

