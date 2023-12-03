import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MensajeMuroService from './mensaje-muro.service';
import { useDateFormat } from '@/shared/composables';
import { type IMensajeMuro } from '@/shared/model/mensaje-muro.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MensajeMuroDetails',
  setup() {
    const dateFormat = useDateFormat();
    const mensajeMuroService = inject('mensajeMuroService', () => new MensajeMuroService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mensajeMuro: Ref<IMensajeMuro> = ref({});

    const retrieveMensajeMuro = async mensajeMuroId => {
      try {
        const res = await mensajeMuroService().find(mensajeMuroId);
        mensajeMuro.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mensajeMuroId) {
      retrieveMensajeMuro(route.params.mensajeMuroId);
    }

    return {
      ...dateFormat,
      alertService,
      mensajeMuro,

      previousState,
      t$: useI18n().t,
    };
  },
});
