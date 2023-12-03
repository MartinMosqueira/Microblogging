/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MensajeMuro from './mensaje-muro.vue';
import MensajeMuroService from './mensaje-muro.service';
import AlertService from '@/shared/alert/alert.service';

type MensajeMuroComponentType = InstanceType<typeof MensajeMuro>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MensajeMuro Management Component', () => {
    let mensajeMuroServiceStub: SinonStubbedInstance<MensajeMuroService>;
    let mountOptions: MountingOptions<MensajeMuroComponentType>['global'];

    beforeEach(() => {
      mensajeMuroServiceStub = sinon.createStubInstance<MensajeMuroService>(MensajeMuroService);
      mensajeMuroServiceStub.retrieve.resolves({ headers: {} });

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
          mensajeMuroService: () => mensajeMuroServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mensajeMuroServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MensajeMuro, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(mensajeMuroServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.mensajeMuros[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MensajeMuroComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MensajeMuro, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        mensajeMuroServiceStub.retrieve.reset();
        mensajeMuroServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        mensajeMuroServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMensajeMuro();
        await comp.$nextTick(); // clear components

        // THEN
        expect(mensajeMuroServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(mensajeMuroServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
