<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="microbloggingApp.applicationUser.home.createOrEditLabel"
          data-cy="ApplicationUserCreateUpdateHeading"
          v-text="t$('microbloggingApp.applicationUser.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="applicationUser.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationUser.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('microbloggingApp.applicationUser.fechaNacimiento')"
              for="application-user-fechaNacimiento"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-user-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="application-user-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('microbloggingApp.applicationUser.telefono')"
              for="application-user-telefono"
            ></label>
            <input
              type="number"
              class="form-control"
              name="telefono"
              id="application-user-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model.number="v$.telefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('microbloggingApp.applicationUser.user')" for="application-user-user"></label>
            <select class="form-control" id="application-user-user" data-cy="user" name="user" v-model="applicationUser.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="applicationUser.user && userOption.id === applicationUser.user.id ? applicationUser.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('microbloggingApp.applicationUser.contacto')" for="application-user-contacto"></label>
            <select
              class="form-control"
              id="application-user-contactos"
              data-cy="contacto"
              multiple
              name="contacto"
              v-if="applicationUser.contactos !== undefined"
              v-model="applicationUser.contactos"
            >
              <option
                v-bind:value="getSelected(applicationUser.contactos, applicationUserOption)"
                v-for="applicationUserOption in applicationUsers"
                :key="applicationUserOption.id"
              >
                {{ applicationUserOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('microbloggingApp.applicationUser.seguido')" for="application-user-seguido"></label>
            <select
              class="form-control"
              id="application-user-seguidos"
              data-cy="seguido"
              multiple
              name="seguido"
              v-if="applicationUser.seguidos !== undefined"
              v-model="applicationUser.seguidos"
            >
              <option
                v-bind:value="getSelected(applicationUser.seguidos, applicationUserOption)"
                v-for="applicationUserOption in applicationUsers"
                :key="applicationUserOption.id"
              >
                {{ applicationUserOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-user-update.component.ts"></script>
