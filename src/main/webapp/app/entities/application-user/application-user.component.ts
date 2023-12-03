import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ApplicationUserService from './application-user.service';
import { type IApplicationUser } from '@/shared/model/application-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationUser',
  setup() {
    const { t: t$ } = useI18n();
    const applicationUserService = inject('applicationUserService', () => new ApplicationUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationUsers: Ref<IApplicationUser[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveApplicationUsers = async () => {
      isFetching.value = true;
      try {
        const res = await applicationUserService().retrieve();
        applicationUsers.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApplicationUsers();
    };

    onMounted(async () => {
      await retrieveApplicationUsers();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IApplicationUser) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeApplicationUser = async () => {
      try {
        await applicationUserService().delete(removeId.value);
        const message = t$('microbloggingApp.applicationUser.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveApplicationUsers();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      applicationUsers,
      handleSyncList,
      isFetching,
      retrieveApplicationUsers,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeApplicationUser,
      t$,
    };
  },
});
