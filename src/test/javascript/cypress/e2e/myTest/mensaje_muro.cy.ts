describe('mensaje muro', () => {
  const page = Cypress.env('baseUrl');

  it('create mensaje', () => {
    cy.visit(page);
    cy.login('user', 'user');
    cy.get('#entity-menu__BV_toggle_ > .navbar-dropdown-menu > .no-bold').click();
    cy.get('div > :nth-child(2) > .dropdown-item').click();
    cy.get('[data-cy="entityCreateButton"] > span').click();
    cy.get('[data-cy="mensaje"]').type('Mensaje creado desde Cypress!!');
    cy.get('[data-cy="fecha"]').type('2024-05-02T08:30');
    cy.get('[data-cy="tags"]').type('tag1');
    cy.get('[data-cy="user"]').select('1051');
    cy.get('[data-cy="entityCreateSaveButton"] > span').click();
  });

  it('update mensaje', () => {
    cy.visit(page);
    cy.login('user', 'user');
    cy.get('#entity-menu__BV_toggle_ > .navbar-dropdown-menu > .no-bold').click();
    cy.get('div > :nth-child(2) > .dropdown-item').click();
    cy.get('table tr:first-of-type .text-right > .btn-group > [data-cy="entityEditButton"]').click();
    cy.get('[data-cy="mensaje"]').clear().type('Hello world Update!!');
    cy.get('[data-cy="entityCreateSaveButton"] > span').click();
  });

  it('delete mensaje ', () => {
    cy.visit(page);
    cy.login('user', 'user');
    cy.get('#entity-menu__BV_toggle_ > .navbar-dropdown-menu > .no-bold').click();
    cy.get('div > :nth-child(2) > .dropdown-item').click();
    cy.get('table tr:first-of-type .text-right > .btn-group > [data-cy="entityDeleteButton"]').click();
    cy.get('[data-cy="entityConfirmDeleteButton"]').click();
  });
});
