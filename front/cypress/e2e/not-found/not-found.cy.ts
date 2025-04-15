/// <reference types="cypress" />
import CypressConfig from "../../../cypress.config";

describe('Not Found Page Tests', () => {
    it('Should display "Page not found!" when navigating to an invalid URL', () => {
        cy.visit('/non-existent-page');
    
        // Логируем текущий URL перед проверкой
        cy.url().then((currentUrl) => {
            cy.log(`📌 Текущий URL: ${currentUrl}`);
        });
    
        // Логируем текст страницы перед поиском заголовка
        cy.get('body').then(($body) => {
            cy.log(`📌 Текст на странице: ${$body.text()}`);
        });
    
        // Проверяем, что пользователь редиректится на 404
        cy.url().should('eq', Cypress.config().baseUrl + '404');
    
        // Проверяем заголовок (ищем не только в h1)
        cy.contains('h1, h2, p, div', 'Page not found !', { timeout: 10000 }).should('be.visible');
    });
    
});
