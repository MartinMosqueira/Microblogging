import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const ApplicationUser = () => import('@/entities/application-user/application-user.vue');
const ApplicationUserUpdate = () => import('@/entities/application-user/application-user-update.vue');
const ApplicationUserDetails = () => import('@/entities/application-user/application-user-details.vue');

const MensajeMuro = () => import('@/entities/mensaje-muro/mensaje-muro.vue');
const MensajeMuroUpdate = () => import('@/entities/mensaje-muro/mensaje-muro-update.vue');
const MensajeMuroDetails = () => import('@/entities/mensaje-muro/mensaje-muro-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'application-user',
      name: 'ApplicationUser',
      component: ApplicationUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-user/new',
      name: 'ApplicationUserCreate',
      component: ApplicationUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-user/:applicationUserId/edit',
      name: 'ApplicationUserEdit',
      component: ApplicationUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'application-user/:applicationUserId/view',
      name: 'ApplicationUserView',
      component: ApplicationUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mensaje-muro',
      name: 'MensajeMuro',
      component: MensajeMuro,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mensaje-muro/new',
      name: 'MensajeMuroCreate',
      component: MensajeMuroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mensaje-muro/:mensajeMuroId/edit',
      name: 'MensajeMuroEdit',
      component: MensajeMuroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mensaje-muro/:mensajeMuroId/view',
      name: 'MensajeMuroView',
      component: MensajeMuroDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
