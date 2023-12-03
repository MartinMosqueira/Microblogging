<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationUserHeading">
      <span v-text="t$('microbloggingApp.applicationUser.home.title')" id="application-user-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('microbloggingApp.applicationUser.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ApplicationUserCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-user"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('microbloggingApp.applicationUser.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationUsers && applicationUsers.length === 0">
      <span v-text="t$('microbloggingApp.applicationUser.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="applicationUsers && applicationUsers.length > 0">
      <table class="table table-striped" aria-describedby="applicationUsers">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.applicationUser.fechaNacimiento')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.applicationUser.telefono')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.applicationUser.user')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.applicationUser.contacto')"></span></th>
            <th scope="row"><span v-text="t$('microbloggingApp.applicationUser.seguido')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationUser in applicationUsers" :key="applicationUser.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: applicationUser.id } }">{{
                applicationUser.id
              }}</router-link>
            </td>
            <td>{{ applicationUser.fechaNacimiento }}</td>
            <td>{{ applicationUser.telefono }}</td>
            <td>
              {{ applicationUser.user ? applicationUser.user.id : '' }}
            </td>
            <td>
              <span v-for="(contacto, i) in applicationUser.contactos" :key="contacto.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'ApplicationUserView', params: { applicationUserId: contacto.id } }"
                  >{{ contacto.id }}</router-link
                >
              </span>
            </td>
            <td>
              <span v-for="(seguido, i) in applicationUser.seguidos" :key="seguido.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'ApplicationUserView', params: { applicationUserId: seguido.id } }">{{
                  seguido.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationUserView', params: { applicationUserId: applicationUser.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationUserEdit', params: { applicationUserId: applicationUser.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(applicationUser)"
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
          id="microbloggingApp.applicationUser.delete.question"
          data-cy="applicationUserDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-applicationUser-heading" v-text="t$('microbloggingApp.applicationUser.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-applicationUser"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeApplicationUser()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application-user.component.ts"></script>
