import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('MensajeMuro e2e test', () => {
  const mensajeMuroPageUrl = '/mensaje-muro';
  const mensajeMuroPageUrlPattern = new RegExp('/mensaje-muro(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mensajeMuroSample = {};

  let mensajeMuro;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mensaje-muros+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mensaje-muros').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mensaje-muros/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mensajeMuro) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mensaje-muros/${mensajeMuro.id}`,
      }).then(() => {
        mensajeMuro = undefined;
      });
    }
  });

  it('MensajeMuros menu should load MensajeMuros page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mensaje-muro');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MensajeMuro').should('exist');
    cy.url().should('match', mensajeMuroPageUrlPattern);
  });

  describe('MensajeMuro page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mensajeMuroPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MensajeMuro page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mensaje-muro/new$'));
        cy.getEntityCreateUpdateHeading('MensajeMuro');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mensajeMuroPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mensaje-muros',
          body: mensajeMuroSample,
        }).then(({ body }) => {
          mensajeMuro = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mensaje-muros+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mensajeMuro],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mensajeMuroPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MensajeMuro page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mensajeMuro');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mensajeMuroPageUrlPattern);
      });

      it('edit button click should load edit MensajeMuro page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MensajeMuro');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mensajeMuroPageUrlPattern);
      });

      it('edit button click should load edit MensajeMuro page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MensajeMuro');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mensajeMuroPageUrlPattern);
      });

      it('last delete button click should delete instance of MensajeMuro', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mensajeMuro').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mensajeMuroPageUrlPattern);

        mensajeMuro = undefined;
      });
    });
  });

  describe('new MensajeMuro page', () => {
    beforeEach(() => {
      cy.visit(`${mensajeMuroPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MensajeMuro');
    });

    it('should create an instance of MensajeMuro', () => {
      cy.get(`[data-cy="mensaje"]`).type('lighthearted grandmother what');
      cy.get(`[data-cy="mensaje"]`).should('have.value', 'lighthearted grandmother what');

      cy.get(`[data-cy="fecha"]`).type('2023-12-02T18:19');
      cy.get(`[data-cy="fecha"]`).blur();
      cy.get(`[data-cy="fecha"]`).should('have.value', '2023-12-02T18:19');

      cy.get(`[data-cy="tags"]`).type('holler unto fooey');
      cy.get(`[data-cy="tags"]`).should('have.value', 'holler unto fooey');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mensajeMuro = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mensajeMuroPageUrlPattern);
    });
  });
});
