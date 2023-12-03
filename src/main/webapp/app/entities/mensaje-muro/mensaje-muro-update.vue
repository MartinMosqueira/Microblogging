<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="microbloggingApp.mensajeMuro.home.createOrEditLabel"
          data-cy="MensajeMuroCreateUpdateHeading"
          v-text="t$('microbloggingApp.mensajeMuro.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mensajeMuro.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mensajeMuro.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('microbloggingApp.mensajeMuro.mensaje')" for="mensaje-muro-mensaje"></label>
            <input
              type="text"
              class="form-control"
              name="mensaje"
              id="mensaje-muro-mensaje"
              data-cy="mensaje"
              :class="{ valid: !v$.mensaje.$invalid, invalid: v$.mensaje.$invalid }"
              v-model="v$.mensaje.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('microbloggingApp.mensajeMuro.fecha')" for="mensaje-muro-fecha"></label>
            <div class="d-flex">
              <input
                id="mensaje-muro-fecha"
                data-cy="fecha"
                type="datetime-local"
                class="form-control"
                name="fecha"
                :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
                :value="convertDateTimeFromServer(v$.fecha.$model)"
                @change="updateInstantField('fecha', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('microbloggingApp.mensajeMuro.tags')" for="mensaje-muro-tags"></label>
            <input
              type="text"
              class="form-control"
              name="tags"
              id="mensaje-muro-tags"
              data-cy="tags"
              :class="{ valid: !v$.tags.$invalid, invalid: v$.tags.$invalid }"
              v-model="v$.tags.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('microbloggingApp.mensajeMuro.user')" for="mensaje-muro-user"></label>
            <select class="form-control" id="mensaje-muro-user" data-cy="user" name="user" v-model="mensajeMuro.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  mensajeMuro.user && applicationUserOption.id === mensajeMuro.user.id ? mensajeMuro.user : applicationUserOption
                "
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
<script lang="ts" src="./mensaje-muro-update.component.ts"></script>
