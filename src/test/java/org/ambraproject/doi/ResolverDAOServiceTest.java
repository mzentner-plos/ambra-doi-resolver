/* $HeadURL:: $
 * $Id: ResolverDAOServiceTest.java 10266 2012-02-09 19:41:41Z akudlick $
 *
 * Copyright (c) 2006-2010 by Public Library of Science
 * http://plos.org
 * http://ambraproject.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.doi;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test for DOI resolver. Dataproviders use values from the sql used to set up the test database
 * (create-resolver-test-db.sql)
 *
 * @author Alex Kudlick
 */
public class ResolverDAOServiceTest extends BaseResolverTest {

  private ResolverDAOService resolverDAOService;

  @BeforeClass(dependsOnMethods = "createDB")
  public void setup() {
    insertArticleRow(1, "info:doi/test-article-1");
    insertArticleRow(2, "info:doi/test-article-2");
    insertAnnotationRow(1, "info:doi/test-annotation-1", 1, "Comment");
    insertAnnotationRow(2, "info:doi/test-annotation-2", 1, "Reply");
    insertAnnotationRow(3, "info:doi/test-annotation-3", 2, "Rating");
    insertAnnotationRow(4, "info:doi/test-annotation-4", 2, "FormalCorrection");

    resolverDAOService = new JdbcResolverService(dataSource);
  }

  @DataProvider(name = "articleDois")
  public Object[][] getArticleDois() {
    return new Object[][]{
        {"info:doi/test-article-1", true},
        {"info:doi/test-article-2", true},
        {"info:doi/test-annotation-1", false},
        {"info:doi/bogus-doi", false}
    };
  }

  @Test(dataProvider = "articleDois")
  public void testIsArticle(String doi, boolean isArticle) {
    if (isArticle) {
      assertTrue(resolverDAOService.doiIsArticle(doi), "DOI wasn't correctly determined to be an article");
    } else {
      assertFalse(resolverDAOService.doiIsArticle(doi), "DOI wasn't correctly determined not to be an article");
    }
  }

  @DataProvider(name = "annotationInfo")
  public Object[][] getAnnotationDois() {
    return new Object[][]{
        {"info:doi/test-annotation-1", new AnnotationInfo(1l, "info:doi/test-article-1", "Comment")},
        {"info:doi/test-annotation-2", new AnnotationInfo(2l, "info:doi/test-article-1", "Reply")},
        {"info:doi/test-annotation-3", new AnnotationInfo(3l, "info:doi/test-article-2", "Rating")},
        {"info:doi/test-annotation-4", new AnnotationInfo(4l, "info:doi/test-article-2", "FormalCorrection")},
        {"info:doi/test-article-1", null},
        {"info:doi/bogus-doi", null},
    };
  }

  @Test(dataProvider = "annotationInfo")
  public void testGetAnnotationInfo(String doi, AnnotationInfo annotationInfo) {
    assertEquals(resolverDAOService.getAnnotationInfo(doi), annotationInfo, "Returned incorrect annotation info");
  }
}
