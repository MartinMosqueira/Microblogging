<template>
  <div>
    <h2 id="page-heading" data-cy="MensajeMuroHeading">
      <span v-text="t$('microbloggingApp.mensajeMuro.home.title')" id="mensaje-muro-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('microbloggingApp.mensajeMuro.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MensajeMuroCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-mensaje-muro"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('microbloggingApp.mensajeMuro.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mensajeMuros && mensajeMuros.length === 0">
      <span v-text="t$('microbloggingApp.mensajeMuro.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mensajeMuros && mensajeMuros.length > 0">
      <table class="table table-striped" aria-describedby="mensajeMuros">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.mensajeMuro.mensaje')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.mensajeMuro.fecha')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.mensajeMuro.tags')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.mensajeMuro.user')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mensajeMuro in mensajeMuros" :key="mensajeMuro.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MensajeMuroView', params: { mensajeMuroId: mensajeMuro.id } }">{{ mensajeMuro.id }}</router-link>
            </td>
            <td>{{ mensajeMuro.mensaje }}</td>
            <td>{{ formatDateShort(mensajeMuro.fecha) || '' }}</td>
            <td>{{ mensajeMuro.tags }}</td>
            <td>
              <div v-if="mensajeMuro.user">
                <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: mensajeMuro.user.id } }">{{
                  mensajeMuro.user.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MensajeMuroView', params: { mensajeMuroId: mensajeMuro.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MensajeMuroEdit', params: { mensajeMuroId: mensajeMuro.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(mensajeMuro)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="microbloggingApp.mensajeMuro.delete.question"
          data-cy="mensajeMuroDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mensajeMuro-heading" v-text="t$('microbloggingApp.mensajeMuro.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mensajeMuro"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeMensajeMuro()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./mensaje-muro.component.ts"></script>
