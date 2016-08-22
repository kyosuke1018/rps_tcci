/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storegateway.test;

import com.tcci.tccstore.model.article.Article;

/**
 *
 * @author Jimmy.Lee
 */
public class ArticleTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        ArticleTest test = new ArticleTest();
        test.login("c1", "admin");
        test.list();
    }
    
    public void list() {
        String service = "/article/list";
        executeGet(service, Article[].class, "文章清單");
    }
    
}
