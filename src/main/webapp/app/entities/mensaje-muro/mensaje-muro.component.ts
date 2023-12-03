import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import MensajeMuroService from './mensaje-muro.service';
import { type IMensajeMuro } from '@/shared/model/mensaje-muro.model';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MensajeMuro',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const mensajeMuroService = inject('mensajeMuroService', () => new MensajeMuroService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mensajeMuros: Ref<IMensajeMuro[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMensajeMuros = async () => {
      isFetching.value = true;
      try {
        const res = await mensajeMuroService().retrieve();
        mensajeMuros.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMensajeMuros();
    };

    onMounted(async () => {
      await retrieveMensajeMuros();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMensajeMuro) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMensajeMuro = async () => {
      try {
        await mensajeMuroService().delete(removeId.value);
        const message = t$('microbloggingApp.mensajeMuro.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMensajeMuros();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      mensajeMuros,
      handleSyncList,
      isFetching,
      retrieveMensajeMuros,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMensajeMuro,
      t$,
    };
  },
});
