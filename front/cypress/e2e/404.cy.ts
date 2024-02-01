/// <reference types="Cypress" />
describe('404 page', () => {
  it('Should show the 404 page when the page does not exist', () => {
    cy.visit('/doesntexist');

    cy.get('h1').should('contain', 'Page not found !');
  });
});
