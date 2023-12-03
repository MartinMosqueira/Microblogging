/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ApplicationUser from './application-user.vue';
import ApplicationUserService from './application-user.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationUserComponentType = InstanceType<typeof ApplicationUser>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ApplicationUser Management Component', () => {
    let applicationUserServiceStub: SinonStubbedInstance<ApplicationUserService>;
    let mountOptions: MountingOptions<ApplicationUserComponentType>['global'];

    beforeEach(() => {
      applicationUserServiceStub = sinon.createStubInstance<ApplicationUserService>(ApplicationUserService);
      applicationUserServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          applicationUserService: () => applicationUserServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationUserServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ApplicationUser, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(applicationUserServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.applicationUsers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ApplicationUserComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ApplicationUser, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        applicationUserServiceStub.retrieve.reset();
        applicationUserServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        applicationUserServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeApplicationUser();
        await comp.$nextTick(); // clear components

        // THEN
        expect(applicationUserServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(applicationUserServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
