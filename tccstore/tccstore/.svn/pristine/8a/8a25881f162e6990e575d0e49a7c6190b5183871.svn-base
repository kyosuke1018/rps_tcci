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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;

/**
 *
 * @author Jimmy.Lee
 */
@Path("article")
public class ArticleService extends ServiceBase {

    @EJB
    private EcArticleFacade ecArticleFacade;
    @Context
    private ServletContext servletContext;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> list() {
        List<Article> articles = new ArrayList<>();
        List<EcArticle> entities = ecArticleFacade.findActive();
        for (EcArticle entity : entities) {
            Article article = EntityToModel.buildArticle(entity);
            if (article.getLink() == null) {
                article.setLink("/tccstore/service/article/view/" + article.getId()); // 手機自行補 host
            }
            articles.add(article);
        }
        return articles;
    }

    @GET
    @Path("view/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response view(@PathParam("id") long id, @QueryParam("preview") String preview) throws URISyntaxException, IOException {
        EcArticle ecArticle = ecArticleFacade.find(id);
        if (null == ecArticle || (!ecArticle.isActive() && !"Y".equalsIgnoreCase(preview))) {
            return Response.status(404).build();
        }
        String link = ecArticle.getLink();
        if (link != null) {
            return Response.temporaryRedirect(new URI(link)).build();
        }
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("title", ecArticle.getTitle());
        valuesMap.put("content", ecArticle.getContent());
        String template = IOUtils.toString(servletContext.getResourceAsStream("/article_template.html"), "UTF-8");
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        return Response.status(200).entity(sub.replace(template)).build();
    }

}
