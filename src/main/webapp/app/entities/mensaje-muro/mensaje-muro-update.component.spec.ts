/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import MensajeMuroUpdate from './mensaje-muro-update.vue';
import MensajeMuroService from './mensaje-muro.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import ApplicationUserService from '@/entities/application-user/application-user.service';

type MensajeMuroUpdateComponentType = InstanceType<typeof MensajeMuroUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mensajeMuroSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MensajeMuroUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MensajeMuro Management Update Component', () => {
    let comp: MensajeMuroUpdateComponentType;
    let mensajeMuroServiceStub: SinonStubbedInstance<MensajeMuroService>;

    beforeEach(() => {
      route = {};
      mensajeMuroServiceStub = sinon.createStubInstance<MensajeMuroService>(MensajeMuroService);
      mensajeMuroServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          mensajeMuroService: () => mensajeMuroServiceStub,
          applicationUserService: () =>
            sinon.createStubInstance<ApplicationUserService>(ApplicationUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(MensajeMuroUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MensajeMuroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mensajeMuro = mensajeMuroSample;
        mensajeMuroServiceStub.update.resolves(mensajeMuroSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mensajeMuroServiceStub.update.calledWith(mensajeMuroSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mensajeMuroServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MensajeMuroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mensajeMuro = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mensajeMuroServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mensajeMuroServiceStub.find.resolves(mensajeMuroSample);
        mensajeMuroServiceStub.retrieve.resolves([mensajeMuroSample]);

        // WHEN
        route = {
          params: {
            mensajeMuroId: '' + mensajeMuroSample.id,
          },
        };
        const wrapper = shallowMount(MensajeMuroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mensajeMuro).toMatchObject(mensajeMuroSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mensajeMuroServiceStub.find.resolves(mensajeMuroSample);
        const wrapper = shallowMount(MensajeMuroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
