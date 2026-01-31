package dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.dtos.MagicAuthRequestDto;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.dtos.MagicAuthResponseDto;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.dtos.MagicLinkAuthOptionQueryDTO;
import dev.sudhanshu.auth_n_authz.controllers.v1.authentication.magiclink.endpoints.MagicAuthEndpoints;
import dev.sudhanshu.auth_n_authz.libs.payload.APIResponse;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.MagicLinkAuthService;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.AuthReqType;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CompleteMagicAuthSessionCommand;
import dev.sudhanshu.auth_n_authz.services.authentication.magiclink.commands.CreateMagicLinkAuthSessionCommand;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// TODO: add response-body based auth support, for now default is cookie based with default options.
@RestController
@RequestMapping(MagicAuthEndpoints.BASE)
public class MagicLinkAuthController {

    @Autowired
    MagicLinkAuthService magicLinkAuthService;

    @PostMapping
    public ResponseEntity<APIResponse<Void>> magicAuth(
        @RequestBody @Valid MagicAuthRequestDto magicAuthRequestDto,
        @ModelAttribute @Valid MagicLinkAuthOptionQueryDTO magicLinkAuthOptionQueryDTO
    ) {

        var isStatefulAuthReq = magicLinkAuthOptionQueryDTO.stateful();

        var cmd = new CreateMagicLinkAuthSessionCommand(
            magicAuthRequestDto.email(),
            (
                isStatefulAuthReq == null ||
                isStatefulAuthReq.equals("false")
            ) ? AuthReqType.STATELESS : AuthReqType.STATEFUL
        );

        magicLinkAuthService.createMagicAuthSession(cmd);

        return ResponseEntity.ok(
            APIResponse.success(null)
        );
    }

    
    @GetMapping(MagicAuthEndpoints.COMPLETE_MAGIC_AUTH)
    public ResponseEntity<APIResponse<MagicAuthResponseDto>> completeMagicAuth(
        @PathVariable("magicId") String magicId        
    ) {
        var cmd = new CompleteMagicAuthSessionCommand(magicId);

        var result = magicLinkAuthService.completeMagicAuthSession(cmd);


        var response = new MagicAuthResponseDto(result.token());

        return ResponseEntity.ok(
            APIResponse.success(response)
        );
    }
}
