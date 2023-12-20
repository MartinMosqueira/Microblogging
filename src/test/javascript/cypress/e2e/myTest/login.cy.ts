describe('login page', () => {
  const page = Cypress.env('baseUrl');
  const usernameAdmin = 'admin';
  const passwordAdmin = 'admin';
  const usernameUser = 'user';
  const passwordUser = 'user';

  it('admin', () => {
    cy.visit(page);
    cy.get('.navbar-dropdown-menu > .no-bold').click();
    cy.get('[data-cy="login"] > span').click();
    cy.get('[data-cy="username"]').type(usernameAdmin);
    cy.get('[data-cy="password"]').type(passwordAdmin);
    cy.get('.btn').click();
  });

  it('user', () => {
    cy.visit(page);
    cy.get('.navbar-dropdown-menu > .no-bold').click();
    cy.get('[data-cy="login"] > span').click();
    cy.get('[data-cy="username"]').type(usernameUser);
    cy.get('[data-cy="password"]').type(passwordUser);
    cy.get('.btn').click();
  });
});
