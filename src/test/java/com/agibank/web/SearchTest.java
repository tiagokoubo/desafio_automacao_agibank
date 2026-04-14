package com.agibank.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;

import com.agibank.pages.HomePage;
import com.agibank.utils.Base;
import com.agibank.utils.ReportListener;

@Tag("web")
@ExtendWith(ReportListener.class)
public class SearchTest extends Base {
    private HomePage homePage;

    @BeforeEach
    public void setupTest() {
        homePage = new HomePage(driver);
    }

    @Test
    @DisplayName("Deve encontrar artigos ao pesquisar termo válido")
    public void shouldFindArticlesWhenSearchingForValidTerm() {
        String searchTerm = "cdb";

        homePage.openSearch();
        homePage.searchFor(searchTerm);
        
        List<String> titles = homePage.getArticleTitles();

        assertTrue(!titles.isEmpty());

        boolean termFound = titles.stream()
                                   .anyMatch(t -> t.toLowerCase().contains(searchTerm.toLowerCase()));

        assertTrue(termFound, "Nenhum dos artigos encontrados contém o termo: " + searchTerm);
    }

    @Test
    @DisplayName("Deve exibir mensagem de erro ao pesquisar termo inexistente")
    public void shouldDisplayErrorMessageWhenSearchingForNonExistentTerm() {
        String searchTerm = "cdbb";

        homePage.openSearch();
        homePage.searchFor(searchTerm);
        
        String actualMessage = homePage.getNoResultsMessage();

        assertEquals("Lamentamos, mas nada foi encontrado para sua pesquisa, tente novamente com outras palavras.",
                actualMessage, "A mensagem de erro deveria informar que nada foi encontrado.");
    }
}