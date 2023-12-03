import { defineComponent, provide } from 'vue';

import ApplicationUserService from './application-user/application-user.service';
import MensajeMuroService from './mensaje-muro/mensaje-muro.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('applicationUserService', () => new ApplicationUserService());
    provide('mensajeMuroService', () => new MensajeMuroService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
