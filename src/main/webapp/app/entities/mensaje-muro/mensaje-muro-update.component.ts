import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MensajeMuroService from './mensaje-muro.service';
import { useValidation, useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';
import { type IApplicationUser } from '@/shared/model/application-user.model';
import { type IMensajeMuro, MensajeMuro } from '@/shared/model/mensaje-muro.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MensajeMuroUpdate',
  setup() {
    const mensajeMuroService = inject('mensajeMuroService', () => new MensajeMuroService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mensajeMuro: Ref<IMensajeMuro> = ref(new MensajeMuro());

    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());

    const applicationUsers: Ref<IApplicationUser[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMensajeMuro = async mensajeMuroId => {
      try {
        const res = await mensajeMuroService().find(mensajeMuroId);
        res.fecha = new Date(res.fecha);
        mensajeMuro.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mensajeMuroId) {
      retrieveMensajeMuro(route.params.mensajeMuroId);
    }

    const initRelationships = () => {
      applicationUserService()
        .retrieve()
        .then(res => {
          applicationUsers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      mensaje: {},
      fecha: {},
      tags: {},
      user: {},
    };
    const v$ = useVuelidate(validationRules, mensajeMuro as any);
    v$.value.$validate();

    return {
      mensajeMuroService,
      alertService,
      mensajeMuro,
      previousState,
      isSaving,
      currentLanguage,
      applicationUsers,
      v$,
      ...useDateFormat({ entityRef: mensajeMuro }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mensajeMuro.id) {
        this.mensajeMuroService()
          .update(this.mensajeMuro)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('microbloggingApp.mensajeMuro.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mensajeMuroService()
          .create(this.mensajeMuro)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('microbloggingApp.mensajeMuro.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
