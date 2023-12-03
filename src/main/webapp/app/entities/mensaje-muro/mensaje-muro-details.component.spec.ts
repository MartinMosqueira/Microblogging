/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MensajeMuroDetails from './mensaje-muro-details.vue';
import MensajeMuroService from './mensaje-muro.service';
import AlertService from '@/shared/alert/alert.service';

type MensajeMuroDetailsComponentType = InstanceType<typeof MensajeMuroDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mensajeMuroSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MensajeMuro Management Detail Component', () => {
    let mensajeMuroServiceStub: SinonStubbedInstance<MensajeMuroService>;
    let mountOptions: MountingOptions<MensajeMuroDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mensajeMuroServiceStub = sinon.createStubInstance<MensajeMuroService>(MensajeMuroService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          mensajeMuroService: () => mensajeMuroServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mensajeMuroServiceStub.find.resolves(mensajeMuroSample);
        route = {
          params: {
            mensajeMuroId: '' + 123,
          },
        };
        const wrapper = shallowMount(MensajeMuroDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mensajeMuro).toMatchObject(mensajeMuroSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mensajeMuroServiceStub.find.resolves(mensajeMuroSample);
        const wrapper = shallowMount(MensajeMuroDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
