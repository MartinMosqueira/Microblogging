<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="applicationUser">
        <h2 class="jh-entity-heading" data-cy="applicationUserDetailsHeading">
          <span v-text="t$('microbloggingApp.applicationUser.detail.title')"></span> {{ applicationUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('microbloggingApp.applicationUser.fechaNacimiento')"></span>
          </dt>
          <dd>
            <span>{{ applicationUser.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.applicationUser.telefono')"></span>
          </dt>
          <dd>
            <span>{{ applicationUser.telefono }}</span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.applicationUser.user')"></span>
          </dt>
          <dd>
            {{ applicationUser.user ? applicationUser.user.id : '' }}
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.applicationUser.contacto')"></span>
          </dt>
          <dd>
            <span v-for="(contacto, i) in applicationUser.contactos" :key="contacto.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: contacto.id } }">{{ contacto.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('microbloggingApp.applicationUser.seguido')"></span>
          </dt>
          <dd>
            <span v-for="(seguido, i) in applicationUser.seguidos" :key="seguido.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ApplicationUserView', params: { applicationUserId: seguido.id } }">{{ seguido.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="applicationUser.id"
          :to="{ name: 'ApplicationUserEdit', params: { applicationUserId: applicationUser.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-user-details.component.ts"></script>
