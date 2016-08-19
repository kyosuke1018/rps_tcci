/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.article;

import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.facade.article.EcArticleFacade;
import com.tcci.tccstore.model.article.Article;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("article")
public class ArticleService extends ServiceBase {

    @EJB
    private EcArticleFacade ecArticleFacade;
    
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> list() {
        List<Article> articles = new ArrayList<>();
        List<EcArticle> entities = ecArticleFacade.findActive();
        for (EcArticle entity : entities) {
            articles.add(EntityToModel.buildArticle(entity));
        }
        return articles;
    }

}
